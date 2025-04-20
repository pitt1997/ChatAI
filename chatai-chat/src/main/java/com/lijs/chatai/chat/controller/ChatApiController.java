package com.lijs.chatai.chat.controller;

import com.lijs.chatai.chat.service.client.impl.DeepSeekClient;
import com.lijs.chatai.chat.model.request.ChatRequest;
import com.lijs.chatai.chat.service.ChatService;
import com.lijs.chatai.common.base.session.SessionUser;
import com.lijs.chatai.common.base.token.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ljs
 * @date 2025-03-10
 * @description
 */
@RestController
@RequestMapping("/api/ai")
public class ChatApiController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private final DeepSeekClient deepSeekClient;
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public ChatApiController(DeepSeekClient deepSeekClient) {
        this.deepSeekClient = deepSeekClient;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody ChatRequest request, @RequestHeader("Authorization") String token) {
        // 认证中心解析 JWT 验证权限
        SessionUser sessionUser = jwtTokenProvider.validateUserToken(token);
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(chatService.chat(request));
    }

    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@RequestParam String prompt) {
        SseEmitter emitter = new SseEmitter();
        executor.execute(() -> {
            // SEE 流式回复
            deepSeekClient.streamChatSEE(prompt, emitter);
            emitter.complete();
        });
        return emitter;
    }
}
