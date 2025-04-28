package com.lijs.chatai.chat.service.client;

import org.springframework.web.socket.WebSocketSession;

/**
 * 通用 LLM 接口定义
 *
 * @author ljs
 * @date 2025-04-16
 * @description
 */
public interface LLMClient {

    String getType();

    String chat(String prompt);

    void streamChat(String prompt, WebSocketSession session);

    boolean supports(String modelType);

    default String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

}
