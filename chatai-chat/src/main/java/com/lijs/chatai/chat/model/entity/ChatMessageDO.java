package com.lijs.chatai.chat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ljs
 * @date 2025-04-16
 * @description
 */
@Entity
@Table(name = "call_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String userId;
    private String sessionId;
    private String content;
    private String role; // user 或 assistant
    private Integer tokenCount;
    private String model;
    private int promptTokens;
    private int completionTokens;
    private BigDecimal price;
    private LocalDateTime callTime;

    public ChatMessageDO(String userId, String sessionId, String content, String role, Integer tokenCount, String model) {

    }
}
