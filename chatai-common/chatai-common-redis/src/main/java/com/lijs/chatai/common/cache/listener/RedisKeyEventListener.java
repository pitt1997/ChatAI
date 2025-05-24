package com.lijs.chatai.common.cache.listener;

import com.lijs.chatai.common.cache.service.CaffeineCacheService;
import com.lijs.chatai.common.cache.config.RedissonConfig;
import org.redisson.api.RPatternTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.PatternMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Redis 事件监听器（Redis Keyspace Notifications）
 * 可订阅redis的key过期获取删除事件，进行相应动作的触发，比如维持本地缓存和redis缓存的一致性
 * 存在问题：需要注意场景当key实际过期但是并未在内存中删除时
 * 惰性清除。当这个key过期之后，访问时，这个Key才会被清除
 * 定时清除。后台会定期检查一部分key，如果有key过期了，就会被清除
 *
 * @author ljs
 * @date 2024-12-26
 * @description
 */
@Component
//@DependsOn({"redissonConfig", "caffeineCacheService"})
public class RedisKeyEventListener {

    private final Logger logger = LoggerFactory.getLogger(RedisKeyEventListener.class);

    /**
     * 监听所有 key 的事件
     */
    private static final String TOPIC_PATTERN = "__keyevent@0__:*";

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private CaffeineCacheService caffeineCacheService;

    @PostConstruct
    public void init() {
        try {
            initListener();
        } catch (Exception e) {
            logger.error("Listening for Redis key events init error!");
        }
    }

    public RedisKeyEventListener() {
    }

    public RedisKeyEventListener(RedissonClient redissonClient, CaffeineCacheService caffeineCacheService) {
        this.redissonClient = redissonClient;
        this.caffeineCacheService = caffeineCacheService;
    }

    /**
     * 初始化 Redis 事件监听器
     */
    private void initListener() {
        new Thread(() -> {
            RedisKeyEventListener listener = new RedisKeyEventListener(redissonClient, caffeineCacheService);
            listener.startListening();
        }).start();
    }

    public void startListening() {
        RPatternTopic topic = redissonClient.getPatternTopic(TOPIC_PATTERN);
        if (topic == null) {
            logger.error("Listening for Redis key events, topic is empty!");
            return;
        }
        topic.addListener(String.class, (PatternMessageListener<String>) (pattern, channel, key) -> {
            logger.info("Event received: {} - Key: {}", channel, key);
            if (channel.toString().endsWith(":expired") || channel.toString().endsWith(":del")) {
                caffeineCacheService.delete(key);
            }
        });

        logger.info("Listening for Redis key events...");
    }

    public static void main(String[] args) throws IOException {
        RedissonConfig config = new RedissonConfig();
        RedissonClient redissonClient = config.redisson();
        // 需要redis开启支持事件： redis-cli CONFIG GET notify-keyspace-events
        // RedissonConfig.enableKeyspaceNotifications(redissonClient);

        // 初始化 Redis 事件监听器
        new Thread(() -> {
            RedisKeyEventListener listener = new RedisKeyEventListener();
            listener.startListening();
        }).start();

        // 测试 Caffeine 和 Redis 缓存
        // cacheService.put("testKey", "testValue");
        System.out.println("Added key to Caffeine: testKey");
        redissonClient.getBucket("testKey").set("testValue");
        redissonClient.getBucket("testKey").expire(5, TimeUnit.SECONDS); // 设置 5 秒过期
        System.out.println("Added key to Redis: testKey with 5 seconds expiry");

        try {
            Thread.sleep(7000); // 等待 Redis 过期事件
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 关闭 Redisson 客户端
        redissonClient.shutdown();
    }

}
