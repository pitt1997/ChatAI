package com.lijs.fizz.common.dubbo.demo;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ljs
 * @date 2025-01-03
 * @description
 */
@SpringBootApplication
public class DubboDemoConsumerApplication implements CommandLineRunner {

    @DubboReference
    private DemoService demoService;

    public static void main(String[] args) {
        SpringApplication.run(DubboDemoConsumerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String hello = demoService.sayHello("World");
        System.out.println(hello);
    }
}