package com.lijs.chatai.auth.dto;

import lombok.Data;

/**
 * @author ljs
 * @date 2025-02-11
 * @description
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
    private String captcha;
}