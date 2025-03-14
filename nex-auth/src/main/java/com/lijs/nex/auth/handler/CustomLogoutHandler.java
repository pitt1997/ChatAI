package com.lijs.nex.auth.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ljs
 * @date 2025-03-14
 * @description
 */
@Component
public class CustomLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 获取前端传来的 Token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            // 清除 Redis 里的 Token 记录（如果有存储）
            // redisTemplate.delete("auth:token:" + token);
            System.out.println("清除 Token: " + token);
        }
    }
}