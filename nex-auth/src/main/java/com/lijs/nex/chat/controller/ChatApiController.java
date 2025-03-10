package com.lijs.nex.chat.controller;

import com.lijs.nex.chat.request.ChatRequest;
import com.lijs.nex.chat.service.ChatService;
import com.lijs.nex.common.base.session.SessionUser;
import com.lijs.nex.common.base.token.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody ChatRequest request, @RequestHeader("Authorization") String token) {
        // TODO 测试放行
        if (token.startsWith("Bearer ")) {
            return ResponseEntity.ok(chatService.callAIModel(request.getMessage(), request.getModelType()));
        }
        // 认证中心解析 JWT 验证权限
        SessionUser sessionUser = jwtTokenProvider.validateUserToken(token);
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(chatService.callAIModel(request.getMessage(), request.getModelType()));
    }
}
