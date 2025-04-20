package com.lijs.chatai.chat.service.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijs.chatai.chat.config.LlmClientConfig;
import com.lijs.chatai.chat.config.LlmClientsProperties;
import com.lijs.chatai.chat.enums.ModelTypeEnum;
import com.lijs.chatai.chat.service.client.LLMClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

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
@RequiredArgsConstructor
public class DeepSeekR1Client implements LLMClient {

    private static final String MODEL_TYPE = "deepseek";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected final LlmClientsProperties clientsProperties;

    @Override
    public String getType() {
        return ModelTypeEnum.DEEPSEEK_R1.getCode();
    }

    @Override
    public String chat(String prompt) {
        LlmClientConfig config = clientsProperties.getClients().get(getType());
        HttpHeaders headers = buildHeaders(config);
        Map<String, Object> body = buildBody(config, prompt);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(config.getApiUrl(), HttpMethod.POST, request, String.class);
        return extractContent(response.getBody());
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
            boolean isReasoningPhase = false; // 标记当前是否处于思考阶段
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("data: ")) {
                    String json = line.substring(6).trim();
                    if (!json.equals("[DONE]")) {
                        // 解析 JSON 并提取内容
                        Pair<String, String> parsed = parseChunk(json);
                        String messageType = parsed.getKey();
                        String messageContent = parsed.getValue();

                        // 根据类型处理消息
                        if (session.isOpen()) {
                            switch (messageType) {
                                case "REASONING":
                                    isReasoningPhase = true;
                                    session.sendMessage(new TextMessage(
                                            messageContent
                                            //"{\"type\":\"REASONING\",\"data\":\"" + escapeJson(messageContent) + "\"}"
                                    ));
                                    break;
                                case "IDLE":
                                    isReasoningPhase = false;
                                    session.sendMessage(new TextMessage(
                                            "思考完毕\n"
                                            //"{\"type\":\"REASONING_END\",\"data\":\"\"}"
                                    ));
                                    break;
                                case "CONTENT":
                                    session.sendMessage(new TextMessage(
                                            messageContent
                                    ));
                                    break;
                            }
                        }
                    }
                }
            }
            return null;
        });
    }

    @Override
    public boolean supports(String modelType) {
        return true;
    }

    public void streamChatSEE(String prompt, SseEmitter emitter) {
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
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(clientHttpRequest.getBody(), body);
            clientHttpRequest.getHeaders().addAll(headers);
        };

        // 使用 ResponseExtractor 处理响应
        restTemplate.execute(config.getApiUrl(), HttpMethod.POST, requestCallback, response -> {
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
            log.error(e.getMessage(), e);
            return "Error parsing response";
        }
    }


    private enum State {REASONING, CONTENT, IDLE}

    private State currentState = State.IDLE;

    public Pair<String, String> parseChunk(String json) {
        try {
            JsonNode delta = new ObjectMapper()
                    .readTree(json)
                    .path("choices").get(0).path("delta");

            // 1. 处理思考内容
            if (delta.has("reasoning_content")) {
                JsonNode reasoningNode = delta.path("reasoning_content");
                if (reasoningNode.isNull()) {
                    currentState = State.CONTENT; // 切换到输出阶段
                    return Pair.of(State.REASONING.name(), ""); // 发送思考结束信号
                } else {
                    currentState = State.REASONING;
                    return Pair.of(State.REASONING.name(), reasoningNode.asText());
                }
            }

            // 2. 处理正式输出（仅在 CONTENT 状态处理）
            if (currentState == State.CONTENT && delta.has("content")) {
                return Pair.of(State.CONTENT.name(), delta.path("content").asText());
            }

            return Pair.of(State.IDLE.name(), "");
        } catch (Exception e) {
            return Pair.of(State.IDLE.name(), "[ERROR]");
        }
    }

    private String extractContent(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "Error parsing response";
        }
    }

    public HttpHeaders buildHeaders(LlmClientConfig config) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + config.getApiKey());
        return headers;
    }

    public Map<String, Object> buildBody(LlmClientConfig config, String prompt) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", config.getModel()); // deepseek-v3 / deepseek-r1
        body.put("temperature", 1.0); // 可调整温度（默认推荐值 1.0）
        body.put("max_tokens", 2048); // 控制回复长度

        List<Map<String, String>> messages = Collections.singletonList(
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}
        );
        body.put("messages", messages);
        return body;
    }
}