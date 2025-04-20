package com.lijs.chatai.chat.service.client.impl;

import com.lijs.chatai.chat.enums.ModelTypeEnum;
import com.lijs.chatai.chat.service.client.LLMClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ljs
 * @date 2025-03-10
 * @description
 */
@Component
public class LocalLLMClient implements LLMClient {

    private static final String LOCAL_MODEL_URL = "http://localhost:5000/api/chat";

    @Override
    public String getType() {
        return ModelTypeEnum.LOCAL.getCode();
    }

    @Override
    public void streamChat(String prompt, WebSocketSession session) {
    }

    public String chat(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);

        ResponseEntity<String> response = restTemplate.postForEntity(LOCAL_MODEL_URL, requestBody, String.class);
        return response.getBody();
    }

    @Override
    public boolean supports(String modelType) {
        return true;
    }
}
