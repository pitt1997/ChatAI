package com.lijs.nex.chat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author ljs
 * @date 2025-03-10
 * @description
 */
@RestController
public class ChatController {

    @GetMapping("/chat")
    public String chatPage() {
        return "chat"; // 对应 chat.ftl 模板
    }

    /**
     * chat页面
     */
    @GetMapping("/auth/chat")
    public ModelAndView chat(ModelAndView modelAndView) {
        modelAndView.setViewName("ftl/chat");
        return modelAndView;
    }
}

