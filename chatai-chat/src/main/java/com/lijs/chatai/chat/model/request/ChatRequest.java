package com.lijs.chatai.chat.model.request;

import lombok.Data;

/**
 * @author ljs
 * @date 2025-03-10
 * @description
 */
@Data
public class ChatRequest {

    private String message;   // 用户输入的消息

    private String modelType; // 选择的大模型，例如 "chatgpt" 或 "local"

    //private ModelType modelType;

    private String userId;
    private String prompt;
    private String sessionId;
}
