package com.lijs.fizz.common.cache.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author ljs
 * @date 2024-10-09
 * @description
 */
@Configuration
public class CaffeineConfig {

    /**
     * 是否支持二级缓存组合，默认不支持
     */
    @Value("${platform.cache.enable-composite:false}")
    private Boolean enableComposite;

    /**
     * 本地缓存到失效时间配置，单位毫秒
     */
    @Value("${platform.cache.caffeine.ttl:6000}")
    private int localCacheTTL;

    @Value("${platform.cache.caffeine.max-size:100000}")
    private int localCacheMaxSize;

    @Bean(name = "nativeCaffeineCache")
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(localCacheTTL, TimeUnit.MILLISECONDS) // 设置过期时间
                .maximumSize(localCacheMaxSize)
                .build();
    }

    public boolean enableComposite() {
        return enableComposite;
    }

    public int getLocalCacheTTL() {
        return localCacheTTL;
    }

}