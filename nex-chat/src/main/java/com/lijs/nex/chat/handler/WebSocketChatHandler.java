package com.lijs.nex.chat.handler;

import com.lijs.nex.chat.llm.DeepSeekClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

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

    private final DeepSeekClient deepSeekClient;

    public WebSocketChatHandler(DeepSeekClient deepSeekClient) {
        this.deepSeekClient = deepSeekClient;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("WebSocket 连接成功：{}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String userMessage = message.getPayload();
        logger.info("用户发送消息：{}", userMessage);
        try {
            // 调用 AI 大模型接口，并流式返回消息
            deepSeekClient.streamChatWs(userMessage, session);
        } catch (Exception e) {
            session.sendMessage(new TextMessage("AI 服务异常，请稍后重试。"));
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("WebSocket 连接关闭：{}", session.getId());
    }
}