package com.lijs.chatai.chat.service.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijs.chatai.chat.config.LlmClientConfig;
import com.lijs.chatai.chat.config.LlmClientsProperties;
import com.lijs.chatai.chat.enums.ModelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DeepSeek AI 客户端
 * https://api-docs.deepseek.com/zh-cn/
 *
 * @author ljs
 * @date 2025-03-10
 * @description
 */
@Slf4j
@Component
public class DeepSeekV3Client extends DeepSeekR1Client {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DeepSeekV3Client(LlmClientsProperties clientsProperties) {
        super(clientsProperties);
    }

    @Override
    public String getType() {
        return ModelTypeEnum.DEEPSEEK_V3.getCode();
    }

    /**
     * 通过 WebSocket 逐步返回 AI 生成的内容
     */
    @Override
    public void streamChat(String prompt, WebSocketSession session) {
        LlmClientConfig config = clientsProperties.getClients().get(getType());
        HttpHeaders headers = buildHeaders(config);
        Map<String, Object> body = buildBody(config, prompt);
        // 流式
        body.put("stream", true);
        List<Map<String, String>> messages = Collections.singletonList(
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        );
        body.put("messages", messages);

        // 使用 RequestCallback 处理请求
        RequestCallback requestCallback = clientHttpRequest -> {
            objectMapper.writeValue(clientHttpRequest.getBody(), body);
            clientHttpRequest.getHeaders().addAll(headers);
        };

        // 处理响应流
        restTemplate.execute(config.getApiUrl(), HttpMethod.POST, requestCallback, response -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("data: ")) {
                    String json = line.substring(6).trim();
                    if (!json.equals("[DONE]")) {
                        String content = extractContentStream(json);
                        if (content != null && !content.isEmpty() && session.isOpen()) {
                            sendWsJsonMessage(session, isReasoningModel(config.getModel()) ? "REASONING" : "CONTENT", content);
                        }
                    } else {
                        // 收到 [DONE]，推送 DONE 消息
                        if (session.isOpen()) {
                            sendWsJsonMessage(session, "DONE", "");
                        }
                    }
                }
            }
            return null;
        });
    }

    private void sendWsJsonMessage(WebSocketSession session, String type, String data) throws IOException {
        Map<String, String> message = new HashMap<>();
        message.put("type", type);
        message.put("data", data);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
    }

    private boolean isReasoningModel(String model) {
        return "deepseek-r1".equalsIgnoreCase(model);
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
            log.error(e.getMessage(), e);
            return "Error parsing response";
        }
    }

}