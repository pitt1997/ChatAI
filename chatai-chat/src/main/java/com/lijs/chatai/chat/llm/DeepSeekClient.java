package com.lijs.chatai.chat.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private static final String API_KEY = "sk-xxx"; // 替换为你的 API Key
    private static final String MODEL = "deepseek-chat"; // deepseek-v3 / deepseek-r1

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

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

        return extractContent(response.getBody());
    }

    /**
     * 通过 WebSocket 逐步返回 AI 生成的内容
     */
    public void streamChatWs(String prompt, WebSocketSession session) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + API_KEY);

        Map<String, Object> body = new HashMap<>();
        body.put("model", MODEL);
        body.put("temperature", 0.7);
        body.put("max_tokens", 2048);
        body.put("stream", true);

        List<Map<String, String>> messages = Arrays.asList(
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        );
        body.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // 使用 RequestCallback 处理请求
        RequestCallback requestCallback = clientHttpRequest -> {
            objectMapper.writeValue(clientHttpRequest.getBody(), body);
            clientHttpRequest.getHeaders().addAll(headers);
        };

        // 处理响应流
        restTemplate.execute(API_URL, HttpMethod.POST, requestCallback, response -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("data: ")) {
                    String json = line.substring(6).trim();
                    if (!json.equals("[DONE]")) {
                        String content = extractContentStream(json);
                        if (!content.isEmpty() && session.isOpen()) {
                            session.sendMessage(new TextMessage(content)); // 发送给 WebSocket 客户端
                        }
                    }
                }
            }
            return null;
        });
    }

    public void streamChatSEE(String prompt, SseEmitter emitter) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + API_KEY);

        Map<String, Object> body = new HashMap<>();
        body.put("model", MODEL); // deepseek-v3 / deepseek-r1
        body.put("temperature", 0.7); // 可调整温度
        body.put("max_tokens", 2048); // 控制回复长度
        body.put("stream", true); // 启用流式响应

        List<Map<String, String>> messages = Arrays.asList(
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        );
        body.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // 使用 RequestCallback 处理请求
        RequestCallback requestCallback = clientHttpRequest -> {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(clientHttpRequest.getBody(), body);
            clientHttpRequest.getHeaders().addAll(headers);
        };

        // 使用 ResponseExtractor 处理响应
        restTemplate.execute(API_URL, HttpMethod.POST, requestCallback, response -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("data: ")) {
                    String json = line.substring(6).trim();
                    if (!json.equals("[DONE]")) {
                        String content = extractContentStream(json);
                        if (!content.isEmpty()) { // 检查 content 是否为空
                            // 发送数据到客户端
                            emitter.send(SseEmitter.event().data(content));
                        }
                    }
                }
            }
            return null;
        });
    }

    private String extractContentStream(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);
            JsonNode deltaNode = root.path("choices").get(0).path("delta");
            if (deltaNode.has("content")) {
                return deltaNode.path("content").asText();
            }
            return ""; // 如果 delta 中没有 content 字段，返回空字符串
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing response";
        }
    }

    private String extractContent(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing response";
        }
    }
}