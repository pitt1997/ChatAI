package com.lijs.chatai.chat.service.client.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ljs
 * @date 2025-03-10
 * @description
 */
@Component
public class LocalLLMClient {

    private static final String LOCAL_MODEL_URL = "http://localhost:5000/api/chat";

    public String chat(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);

        ResponseEntity<String> response = restTemplate.postForEntity(LOCAL_MODEL_URL, requestBody, String.class);
        return response.getBody();
    }
}
