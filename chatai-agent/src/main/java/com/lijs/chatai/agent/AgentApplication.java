package com.lijs.chatai.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author author
 * @date 2025-05-25
 * @description
 */

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
})
@ImportAutoConfiguration
public class AgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentApplication.class, args);
    }

}
