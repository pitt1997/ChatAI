package com.lijs.chatai.chat.handler;

import com.lijs.chatai.chat.llm.DeepSeekClient;
import com.lijs.chatai.common.base.token.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author author
 * @date 2025-03-11
 * @description
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private DeepSeekClient deepSeekClient;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketChatHandler(deepSeekClient, jwtTokenProvider), "/api/ai/chat/websocket").setAllowedOrigins("*");
    }
}
