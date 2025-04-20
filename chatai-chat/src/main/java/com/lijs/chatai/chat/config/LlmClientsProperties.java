package com.lijs.chatai.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 配置绑定类
 *
 * @author ljs
 * @date 2025-04-16
 * @description
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "llm")
public class LlmClientsProperties {

    private Map<String, LlmClientConfig> clients;

}