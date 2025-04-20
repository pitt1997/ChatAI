package com.lijs.chatai.chat.model.request;

import com.lijs.chatai.chat.enums.ModelType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ljs
 * @date 2025-04-16
 * @description
 */
@Data
public class ChatSession {
    private String id;
    private String userId;
    private String modelType;
    // private ModelType modelType;
    private LocalDateTime createTime;
}