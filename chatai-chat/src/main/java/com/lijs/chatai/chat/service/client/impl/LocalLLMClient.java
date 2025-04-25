package com.lijs.chatai.chat.service.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijs.chatai.chat.config.LlmClientConfig;
import com.lijs.chatai.chat.config.LlmClientsProperties;
import com.lijs.chatai.chat.enums.ModelTypeEnum;
import com.lijs.chatai.chat.service.client.LLMClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ljs
 * @date 2025-03-10
 * @description
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LocalLLMClient implements LLMClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected final LlmClientsProperties clientsProperties;

    @Override
    public String getType() {
        return ModelTypeEnum.LOCAL.getCode();
    }

    @Override
    public void streamChat(String prompt, WebSocketSession session) {
        LlmClientConfig config = clientsProperties.getClients().get(getType());
        // 1. 构建请求头（Ollama 不需要认证头，只需 Content-Type）
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2. 构建请求体（Ollama 的格式与 OpenAI 不同）
        Map<String, Object> body = new HashMap<>();
        body.put("model", config.getModel()); // 指定本地模型
        body.put("prompt", prompt);                 // 直接使用 prompt 字段
        body.put("stream", true);                   // 启用流式

        // 3. 使用 RequestCallback 发送请求
        RequestCallback requestCallback = request -> {
            objectMapper.writeValue(request.getBody(), body);
            request.getHeaders().addAll(headers);
        };

        // 4. 处理 Ollama 的流式响应（非 SSE 格式，直接逐行读 JSON）
        restTemplate.execute(
                config.getApiUrl(), // Ollama 的生成接口
                HttpMethod.POST,
                requestCallback,
                response -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            // 解析每行 JSON（如 {"response":"你","done":false}）
                            JsonNode jsonNode = objectMapper.readTree(line);
                            if (jsonNode.has("response")) {
                                String content = jsonNode.get("response").asText();
                                if (session.isOpen()) {
                                    session.sendMessage(new TextMessage(content));
                                }
                            }
                        }
                    } catch (Exception e) {
                        if (session.isOpen()) {
                            session.close(CloseStatus.SERVER_ERROR);
                        }
                    }
                    return null;
                }
        );
    }

    public String chat(String prompt) {

        LlmClientConfig config = clientsProperties.getClients().get(getType());

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);

        ResponseEntity<String> response = restTemplate.postForEntity(config.getApiUrl(), requestBody, String.class);
        return response.getBody();
    }

    @Override
    public boolean supports(String modelType) {
        return true;
    }
}
