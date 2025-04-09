package com.lijs.chatai.common.cache.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.lijs.chatai.common.cache.config.CaffeineConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author ljs
 * @date 2024-10-10
 * @description
 */
@Component
public class CaffeineCacheService extends BaseService {

    @Autowired
    private CaffeineConfig caffeineConfig;

    @Autowired
    @Qualifier("nativeCaffeineCache")
    private Cache<String, Object> caffeineCache;

    public <T> T get(String key, Class<T> valueClass) {
        T t = (T) caffeineCache.getIfPresent(getKey(key));
        return t;
    }

    public void setWithTTL(String key, Object obj) {
        caffeineCache.put(getKey(key), obj);
    }

    public Object getIfPresent(String key) {
        return caffeineCache.getIfPresent(getKey(key));
    }

    public void set(String key, Object obj) {
        caffeineCache.put(getKey(key), obj);
    }

    public void delete(String key) {
        caffeineCache.invalidate(getKey(key));
    }

    /**
     * 是否开启本地缓存
     *
     * @return 如果环境变量没有配置本地缓存则表示本地缓存不可用
     */
    public boolean enableCaffeineCache() {
        return caffeineConfig.enableComposite();
    }

}
