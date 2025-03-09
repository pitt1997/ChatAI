package com.lijs.fizz.common.cache.service;

import com.lijs.fizz.common.cache.model.CacheNullValue;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 缓存接口封装，支持二级缓存
 * 注意在Redis失效时间较短时不建议使用该方式避免Redis失效但本地缓存还存在，本地缓存缓存失效时间一般设置秒级
 *
 * @author ljs
 * @date 2024-10-10
 * @description
 */
@Component
@ConditionalOnClass(RedissonClient.class)
public class CacheService extends BaseService {

    private static final String LOCK_PREFIX = "LOCK:";

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisCacheService redisCacheService;

    @Resource
    private CaffeineCacheService caffeineCacheService;

    /**
     * 二级缓存获取数据，保证一致性
     */
    public <T> T get(String key, Class<T> valueClass) {
        if (!caffeineCacheService.enableCaffeineCache()) {
            return redisCacheService.get(key, valueClass);
        }
        // 从本地缓存获取
        Object data = caffeineCacheService.getIfPresent(key);
        if (data != null) {
            return (data instanceof CacheNullValue) ? null : (T) data;
        }
        // 获取 Redis 分布式锁，避免并发时多次查询 Redis
        RLock lock = redissonClient.getLock(LOCK_PREFIX + key);
        try {
            // 尝试加锁，等待时间 1000 ms，锁超时时间 5 秒
            if (lock.tryLock(1000, 5000, TimeUnit.MILLISECONDS)) {
                try {
                    // 再次检查本地缓存，避免重复加载
                    data = caffeineCacheService.getIfPresent(key);
                    if (data != null) {
                        return (data instanceof CacheNullValue) ? null : (T) data;
                    }

                    // 从 Redis 中获取数据
                    T t = redisCacheService.get(key, valueClass);
                    data = (t != null) ? redisCacheService.convertValue(t, valueClass) : new CacheNullValue();

                    // 同步本地缓存，注意：不存在时缓存一个"空对象"
                    caffeineCacheService.set(key, data);
                    return (data instanceof CacheNullValue) ? null : (T) data;
                } finally {
                    lock.unlock(); // 释放锁
                }
            } else {
                // 获取锁失败时，直接返回 null 或重试机制
                return null;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public Boolean isExist(String key) {
        if (!caffeineCacheService.enableCaffeineCache()) {
            return redisCacheService.isExist(key);
        }
        Object data = caffeineCacheService.getIfPresent(key);
        if (data != null) {
            return (Boolean) data;
        }
        // 使用分布式锁
        RLock lock = redissonClient.getLock(LOCK_PREFIX + key);
        try {
            if (lock.tryLock(1000, 5000, TimeUnit.MILLISECONDS)) {
                try {
                    data = caffeineCacheService.getIfPresent(key);
                    if (data != null) {
                        return (Boolean) data;
                    }

                    Boolean exist = redisCacheService.isExist(key);
                    caffeineCacheService.set(key, exist);
                    return exist;
                } finally {
                    lock.unlock();
                }
            } else {
                return false;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 保存缓存
     */
    public void set(String key, Object obj) {
        RLock lock = redissonClient.getLock(LOCK_PREFIX + key);
        try {
            lock.lock(5000, TimeUnit.MILLISECONDS); // 加锁，防止并发写入不一致
            caffeineCacheService.set(key, obj);
            redisCacheService.set(key, obj);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 带 TTL 的保存缓存（TTL单位ms）
     */
    public void setWithTTL(String key, Object obj, long ttl) {
        RLock lock = redissonClient.getLock(LOCK_PREFIX + key);
        try {
            lock.lock(5000, TimeUnit.MILLISECONDS);
            caffeineCacheService.set(key, obj);
            redisCacheService.setWithTTL(key, obj, ttl, TimeUnit.MILLISECONDS);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 删除缓存
     */
    public void delete(String key) {
        RLock lock = redissonClient.getLock(LOCK_PREFIX + key);
        try {
            lock.lock(5000, TimeUnit.MILLISECONDS);
            caffeineCacheService.delete(key);
            redisCacheService.delete(key);
        } finally {
            lock.unlock();
        }
    }

}
