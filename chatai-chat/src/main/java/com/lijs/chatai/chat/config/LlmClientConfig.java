package com.lijs.chatai.chat.config;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author author
 * @date 2025-04-20
 * @description
 */

@Data
public class LlmClientConfig {

    private Boolean enabled;

    private String apiUrl;

    private String apiKey;

    private String model;

    private BigDecimal pricePerToken;

}