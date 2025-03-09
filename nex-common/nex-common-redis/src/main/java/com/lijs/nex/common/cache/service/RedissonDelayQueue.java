package com.lijs.nex.common.cache.service;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 延时队列，用于redis中key过期时需要触发相关事件
 * 例如保证内存和redis缓存一致性，或者订单没有进行支付，redis中失效后需要后置逻辑自动取消下发短信
 * 来自：https://zhuanlan.zhihu.com/p/623292076
 *
 * @author ljs
 * @date 2024-12-26
 * @description
 */
@Component
public class RedissonDelayQueue {

    private final Logger logger = LoggerFactory.getLogger(RedissonDelayQueue.class);

    private final static String DELAY_QUEUE_NAME = "redissonDelayQueue";

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private CaffeineCacheService caffeineCache;

    @Value("${platform.delay-queue.enable:false}")
    private Boolean enableDelayQueue;

    private RDelayedQueue<String> delayQueue;
    private RBlockingQueue<String> blockingQueue;

    private final ExecutorService executorService = Executors.newFixedThreadPool(4); // 初始化线程池

    @PostConstruct
    public void init() {
        if(!enableDelayQueue) {
            return;
        }
        initDelayQueue();
        startDelayQueueConsumer();
    }

    private void initDelayQueue() {
        blockingQueue = redissonClient.getBlockingQueue(DELAY_QUEUE_NAME);
        delayQueue = redissonClient.getDelayedQueue(blockingQueue);
    }

    /**
     * 延迟任务的消费线程
     * 线程池避免队列同时阻塞导致由于消费者来不及处理完成耗时操作导致出现"延时"
     */
    private void startDelayQueueConsumer() {
        for (int i = 0; i < 4; i++) { // 启动多个消费线程
            executorService.submit(() -> {
                while (true) {
                    try {
                        String key = blockingQueue.take();
                        logger.info("接收到延迟任务: {}", key);
                        // 清理本地缓存
                        caffeineCache.delete(key);
                        logger.info("本地缓存清理完成: {}", key);
                    } catch (Exception e) {
                        logger.error("延迟队列消费异常", e);
                    }
                }
            });
        }
    }

    /**
     * 接口添加任务
     *
     * @param task    任务名称 （key值）
     * @param seconds 过期时间（延时时间）
     */
    public void offerTask(String task, long seconds) {
        logger.info("添加延迟任务:{} 延迟时间:{}s", task, seconds);
        delayQueue.offer(task, seconds, TimeUnit.SECONDS);
    }

}
