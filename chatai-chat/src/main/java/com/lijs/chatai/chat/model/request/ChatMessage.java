package com.lijs.chatai.chat.model.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ljs
 * @date 2025-04-16
 * @description
 */
@Data
public class ChatMessage {
    private String id;
    private String sessionId;
    private String content;
    private String role; // user 或 assistant
    private Integer tokenCount;
    private LocalDateTime createTime;
}
