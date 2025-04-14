package com.lijs.chatai.chat.handler;

import com.lijs.chatai.chat.llm.DeepSeekClient;
import com.lijs.chatai.common.base.session.SessionUser;
import com.lijs.chatai.common.base.token.JwtTokenProvider;
import com.lijs.chatai.common.base.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * WebSocket 处理 AI 聊天的消息
 *
 * @author author
 * @date 2025-03-11
 * @description
 */
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtTokenProvider jwtTokenProvider;

    private final DeepSeekClient deepSeekClient;

    public WebSocketChatHandler(DeepSeekClient deepSeekClient, JwtTokenProvider jwtTokenProvider) {
        this.deepSeekClient = deepSeekClient;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        HttpHeaders headers = session.getHandshakeHeaders();

        // 1、优先从请求头获取 Token
        String token = extractTokenFromHeaders(headers);
        if (token == null) {
            // 2、如果请求头没有 Token，则从 Cookie 获取
            token = extractTokenFromCookies(headers.get("cookie"));
        }

        if (token == null) {
            // 2、websocket支持从请求参数中传递票据
            token = extractTokenFromRequest(session);
        }

        // 3、校验 Token
        if (!validToken(token)) {
            logger.warn("WebSocket 连接失败，Token 无效：{}", session.getId());
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        logger.info("WebSocket 连接成功，用户身份验证通过：{}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // json: modelType , message
        String userMessage = message.getPayload();
        logger.info("用户发送消息：{}", userMessage);
        try {
            Map map = JsonUtils.toMap(userMessage);
            String modelType = (String) map.get("modelType");
            String prompt = (String) map.get("message");
            // 调用 AI 大模型接口，并流式返回消息
            deepSeekClient.streamChatWs(prompt, session);
        } catch (Exception e) {
            session.sendMessage(new TextMessage("AI 服务异常，请稍后重试。"));
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("WebSocket 连接关闭：{}", session.getId());
    }

    /**
     * 从请求头中提取 Token
     */
    private String extractTokenFromHeaders(HttpHeaders headers) {
        List<String> authHeaders = headers.get("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String authHeader = authHeaders.get(0);
            if (authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
        }
        return null;
    }

    /**
     * 从请求头中提取 Token
     */
    private String extractTokenFromRequest(WebSocketSession session) {
        if (session.getUri() != null) {
            return UriComponentsBuilder.fromUri(session.getUri()).build().getQueryParams().getFirst("WebSocket-Authorization");
        }
        return null;
    }

    /**
     * 从 Cookie 中提取 Token
     */
    private String extractTokenFromCookies(List<String> cookieHeaders) {
        if (cookieHeaders == null || cookieHeaders.isEmpty()) {
            return null;
        }
        for (String cookieHeader : cookieHeaders) {
            String[] cookies = cookieHeader.split("; ");
            for (String cookie : cookies) {
                if (cookie.startsWith("AUTH-TOKEN=")) {
                    return cookie.substring("AUTH-TOKEN=".length());
                }
            }
        }
        return null;
    }

    private boolean validToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        SessionUser sessionUser = jwtTokenProvider.validateUserToken(token);
        return sessionUser != null;
    }
}