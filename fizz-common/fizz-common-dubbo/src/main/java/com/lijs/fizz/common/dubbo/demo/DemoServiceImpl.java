package com.lijs.fizz.common.dubbo.demo;

import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author ljs
 * @date 2025-01-03
 * @description
 */
@DubboService
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}