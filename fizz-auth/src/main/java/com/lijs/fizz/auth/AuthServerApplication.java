package com.lijs.fizz.auth;

import com.lijs.fizz.common.mybatis.config.MybatisPlusConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ljs
 * @date 2025-02-11
 * @description
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.lijs.fizz.core.api.feign") // 指定 Feign 接口所在包
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        MybatisPlusConfig.class
})
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
