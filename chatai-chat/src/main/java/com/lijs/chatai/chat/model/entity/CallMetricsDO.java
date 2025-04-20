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
public class CallMetricsDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String model;
    private int promptTokens;
    private int completionTokens;
    private BigDecimal price;
    private LocalDateTime callTime;

}
