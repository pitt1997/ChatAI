package com.lijs.chatai.chat.service;

import com.lijs.chatai.chat.service.client.LLMClient;
import com.lijs.chatai.chat.model.entity.ChatMessageDO;
import com.lijs.chatai.chat.model.request.ChatMessage;
import com.lijs.chatai.chat.model.request.ChatRequest;
import com.lijs.chatai.chat.model.request.ChatSession;
import com.lijs.chatai.chat.utils.ChatTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ljs
 * @date 2025-03-10
 * @description
 */
@Service
public class ChatService {

    @Autowired
    private List<LLMClient> llmClients;

    @Autowired
    private ChatSessionService chatSessionService;

    @Autowired
    private ChatMessageService chatMessageService;

    public String chat(ChatRequest request) {
        String message = request.getMessage();
        String modelType = request.getModelType();
        ChatSession session = getOrCreateSession(request);
        List<ChatMessage> history = chatMessageService.findLastMessages(session.getId(), 10);
        String prompt = buildPrompt(history, message);
        LLMClient client = llmClients.stream()
                .filter(c -> c.supports(modelType))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("模型不支持"));

        String answer = client.chat(prompt);

        int inputTokens = ChatTokenUtils.count(prompt);
        int outputTokens = ChatTokenUtils.count(answer);

        chatMessageService.insert(new ChatMessageDO("", "", prompt, "user", inputTokens, modelType));
        chatMessageService.insert(new ChatMessageDO("", "", answer, "assistant", outputTokens, modelType));

        return answer;
    }

    private String buildPrompt(List<ChatMessage> history, String newPrompt) {
        StringBuilder sb = new StringBuilder();
        for (ChatMessage msg : history) {
            sb.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
        }
        // TODO 历史会话拼接
        sb.append("user: ").append(newPrompt);
        return sb.toString();
    }

    private ChatSession getOrCreateSession(ChatRequest request) {
        if (request.getSessionId() != null) {
            return chatSessionService.findById(request.getSessionId());
        }
        ChatSession session = new ChatSession();
        session.setUserId(request.getUserId());
        session.setModelType(request.getModelType());
        chatSessionService.insert(session);
        return session;
    }
}
