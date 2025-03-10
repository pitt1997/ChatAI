package com.lijs.nex.chat.llm;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ljs
 * @date 2025-03-10
 * @description DeepSeek AI 客户端
 */
@Component
public class DeepSeekClient {
    private static final String API_URL = "https://api.deepseek.com/chat/completions"; // DeepSeek API 地址
    private static final String API_KEY = "你的 DeepSeek API Key"; // 替换为你的 API Key
    private static final String MODEL = "deepseek-chat"; // deepseek-v3 / deepseek-r1

    public String chat(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + API_KEY);

        Map<String, Object> body = new HashMap<>();
        body.put("model", MODEL); // deepseek-v3 / deepseek-r1
        body.put("temperature", 0.7); // 可调整温度
        body.put("max_tokens", 2048); // 控制回复长度

        List<Map<String, String>> messages = Arrays.asList(
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        );
        body.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, String.class);

        return response.getBody();
    }
}