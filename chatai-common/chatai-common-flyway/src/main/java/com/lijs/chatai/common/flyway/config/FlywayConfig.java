package com.lijs.chatai.common.flyway.config;

import com.lijs.chatai.common.flyway.service.FlywayService;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author ljs
 * @date 2024-12-13
 * @description
 */
@Configuration
public class FlywayConfig {

    @Resource
    private FlywayService flywayService;

    /**
     * 声明一个名为 flywayMigrationStrategy 的 Bean，返回类型是 FlywayMigrationStrategy。
     * 该 Bean 会被 Spring 容器托管，并且在 Flyway 执行迁移时自动生效。
     */
    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> flywayService.migrate();
    }
}