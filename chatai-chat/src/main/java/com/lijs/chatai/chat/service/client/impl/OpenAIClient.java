package com.lijs.chatai.chat.service.client.impl;

import com.lijs.chatai.chat.config.LLMClientsProperties;
import com.lijs.chatai.chat.service.client.LLMClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * OpenAI 实现类
 * @author ljs
 * @date 2025-04-16
 * @description
 */
@Component("openai")
@RequiredArgsConstructor
public class OpenAIClient implements LLMClient {

    private static final String MODEL_TYPE = "openai";

    private final LLMClientsProperties clientsProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getType() {
        return MODEL_TYPE;
    }

    @Override
    public String chat(String prompt) {
        LLMClientsProperties.LLMClientConfig config = clientsProperties.getConfigs().get("openai");

        Map<String, Object> payload = new HashMap<String, Object>() {{
            put("model", config.getModel());
            put("messages", Collections.singletonList(
                    new HashMap<String, Object>() {{
                        put("role", "user");
                        put("content", prompt);
                    }}
            ));
            put("temperature", 0.7);
        }};
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(config.getKey());
//
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(config.getUrl(), entity, String.class);
//        return response.getBody();

        return null;
    }

    @Override
    public void streamChat(String prompt, WebSocketSession session) {
        // Optional: 可实现 SSE 方式调用
        //return "Stream not supported yet";
    }

    @Override
    public boolean supports(String modelType) {
        return false;
    }
}

