package com.lijs.chatai.auth;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.lijs.chatai.common.mybatis.config.MybatisPlusConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ljs
 * @date 2025-02-11
 * @description
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.lijs.chatai.core.api.feign") // 指定 Feign 接口所在包
@ImportAutoConfiguration
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        MybatisPlusAutoConfiguration.class,
        MybatisPlusConfig.class
})
// 注意：AuthServerApplication在微服务环境下才生效!避免服务启动冲突。如果在BootApplication中启动时未进行屏蔽则会导致启动类排除相关配置避免干扰主项目
@ConditionalOnProperty(name = "spring.cloud.nacos.discovery.enabled", havingValue = "true", matchIfMissing = true) // 微服务时加载
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
