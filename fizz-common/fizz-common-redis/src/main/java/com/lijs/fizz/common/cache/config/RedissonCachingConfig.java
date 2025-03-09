package com.lijs.fizz.common.cache.config;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson 缓存管理器
 */
@Configuration
@ConditionalOnClass(RedissonClient.class)
@EnableCaching
public class RedissonCachingConfig implements BeanFactoryPostProcessor {
    private static volatile ConfigurableListableBeanFactory configurableListableBeanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        configurableListableBeanFactory = beanFactory;
    }

    @Bean
    @ConditionalOnMissingBean(name = "cacheManager")
    public CacheManager cacheManager() {
        return new RedissonSpringCacheManager(configurableListableBeanFactory.getBean(RedissonClient.class));
    }
}
