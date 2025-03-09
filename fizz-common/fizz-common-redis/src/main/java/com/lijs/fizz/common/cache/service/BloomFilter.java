package com.lijs.fizz.common.cache.service;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 布隆过滤器（防止缓存穿透）
 * 请求查询数据时，先检查布隆过滤器是否可能包含该 key。
 * 如果布隆过滤器判定该 key 一定不存在，直接返回结果，避免访问 Redis 和数据库。
 * 如果布隆过滤器判定 key 可能存在，则进一步查询 Redis 或数据库。
 * <p>
 * 布隆过滤器的具体场景举例
 * 1. 防止缓存穿透
 * 检测用户是否尝试访问不存在的资源。
 * 布隆过滤器的结果决定是否查询下游 Redis 或数据库。
 * 2. 黑名单过滤
 * 用于快速拦截非法用户请求，例如检测 IP 或 Token。
 * 3. 访问计数
 * 布隆过滤器预过滤 URL 的访问量限制，避免重复计算。
 *
 * @author ljs
 * @date 2024-12-26
 * @description
 */
@Component
public class BloomFilter extends BaseService {

    private final Logger logger = LoggerFactory.getLogger(BloomFilter.class);

    private final static String BLOOM_FILTER = "bloomFilter";

    private final RBloomFilter<String> bloomFilter;

    @Resource
    private CacheService cacheService;

    @Autowired
    public BloomFilter(RedissonClient redissonClient) {
        bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER);
        // 初始化布隆过滤器：预计存储量100000，误判率0.01（1%）
        bloomFilter.tryInit(100_000, 0.01);
    }

    /**
     * 预热布隆过滤器中的key
     */
    @PostConstruct
    public void preheatBloomFilter() {
        // 获取数据库或缓存中已存在的所有有效 key
        List<String> keys = getKeysFromDatabaseOrRedis();
        for (String key : keys) {
            this.add(getKey(key)); // 将这些 key 加入布隆过滤器
        }
        logger.info("布隆过滤器预热完成，共预热数据：{}", keys.size());
    }

    /**
     * 经过布隆过滤器获取对应值防止缓存穿透
     */
    public <T> T getWithBloomFilter(String key, Class<T> valueClass) {
        // 如果布隆过滤器中没有这个 key，直接跳过缓存查询，进行数据库查询
        if (!this.mightContain(key)) {
            // 布隆过滤器判定一定不存在
            return null;
        }

        // 布隆过滤器判断可能存在的 key 继续走缓存查询流程
        return cacheService.get(key, valueClass);
    }

    /**
     * 添加元素到布隆过滤器
     *
     * @param key 元素
     */
    public void add(String key) {
        bloomFilter.add(key);
    }

    /**
     * 检查元素是否可能存在
     *
     * @param key 元素
     * @return true 表示可能存在，false 表示一定不存在
     */
    public boolean mightContain(String key) {
        return bloomFilter.contains(key);
    }

    private List<String> getKeysFromDatabaseOrRedis() {
        return new ArrayList<>();
    }

    /**
     * 动态更新布隆过滤器
     * 当业务数据变化较快时，需要动态更新布隆过滤器以保证数据的准确性：
     * 后台任务：定时从数据库中同步数据更新布隆过滤器。
     * 事件驱动：使用 Redis 的事件通知机制（Keyspace Notifications）或 MQ 监听数据变化，更新布隆过滤器。
     */
//    @Scheduled(fixedRate = 3600000) // 每小时更新一次
//    public void refreshBloomFilter() {
//        List<String> keys = databaseService.getAllKeys();
//        for (String key : keys) {
//            bloomFilterService.add(key);
//        }
//    }

}
