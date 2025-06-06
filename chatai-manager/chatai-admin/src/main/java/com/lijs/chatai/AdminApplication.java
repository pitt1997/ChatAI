package com.lijs.chatai;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 单机版启动器，需要运行此模块则整个系统启动
 *
 * @author ljs
 * @date 2025-01-17
 * @description
 */
@SpringBootApplication(exclude = {MybatisPlusAutoConfiguration.class})
@ImportAutoConfiguration
// markerInterface作用：只有那些直接或间接继承了 BaseMapper 的接口才会被认为是合法的 Mapper 接口。（简化配置不需要@Mapper注解）
@MapperScan(value= "com.lijs.chatai", markerInterface = BaseMapper.class)
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
