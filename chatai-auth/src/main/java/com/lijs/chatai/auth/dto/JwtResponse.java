package com.lijs.chatai.auth.dto;

import lombok.Data;

/**
 * @author ljs
 * @date 2025-02-11
 * @description
 */
@Data
public class JwtResponse {

    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }
}