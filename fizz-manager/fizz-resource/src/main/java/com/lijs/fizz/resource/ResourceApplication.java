package com.lijs.fizz.resource;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.lijs")
// markerInterface作用：只有那些直接或间接继承了 BaseMapper 的接口才会被认为是合法的 Mapper 接口。（简化配置不需要@Mapper注解）
@MapperScan(value= "com.lijs.fizz.resource.dao", markerInterface = BaseMapper.class)
public class ResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceApplication.class, args);
    }

}
