package com.lijs.chatai.chat.config;

import com.lijs.chatai.chat.service.client.LLMClientFactory;
import com.lijs.chatai.chat.websocket.WebSocketChatHandler;
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
    private LLMClientFactory LLMClientFactory;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketChatHandler(LLMClientFactory, jwtTokenProvider), "/api/ai/chat/websocket").setAllowedOrigins("*");
    }
}
