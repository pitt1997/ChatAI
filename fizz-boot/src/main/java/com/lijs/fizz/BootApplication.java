package com.lijs.fizz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 单机版启动器，需要运行此模块则整个系统启动
 *
 * @author ljs
 * @date 2025-01-17
 * @description
 */
@SpringBootApplication
//@ComponentScan(basePackages = {"com.lijs", "com.lijs.fizz", "com.lijs.fizz.auth"})
@ComponentScan(basePackages = {"com.lijs.fizz", "com.lijs.fizz.user", "com.lijs.fizz.auth"})
// markerInterface作用：只有那些直接或间接继承了 BaseMapper 的接口才会被认为是合法的 Mapper 接口。（简化配置不需要@Mapper注解）
@MapperScan(value= "com.lijs.fizz", markerInterface = BaseMapper.class)
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

}
