package com.lijs.fizz.auth.handler;

import com.lijs.fizz.common.base.exception.AccessDeniedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ljs
 * @date 2025-03-05
 * @description
 */
@RestControllerAdvice
public class GlobalAuthExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 403);
        response.put("message", ex.getMessage());

        return ResponseEntity.status(403).body(response);
    }
}