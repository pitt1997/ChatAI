package com.lijs.chatai.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 配置绑定类
 *
 * @author ljs
 * @date 2025-04-16
 * @description
 */
@Configuration
@ConfigurationProperties(prefix = "llm.clients")
@Data
public class LLMClientsProperties {

    private Map<String, LLMClientConfig> configs;

    @Data
    public static class LLMClientConfig {
        private boolean enabled;
        private String apiKey;
        private String apiUrl;
        private String model;
        private BigDecimal pricePerToken;
    }
}
