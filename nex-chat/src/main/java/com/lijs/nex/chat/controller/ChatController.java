package com.lijs.nex.chat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author ljs
 * @date 2025-03-10
 * @description
 */
@RestController
public class ChatController {

    /**
     * chat页面
     */
    @GetMapping("/chat")
    public ModelAndView chat(ModelAndView modelAndView, @RequestParam(value = "modelType", defaultValue = "deepseek") String modelType) {
        // 默认模型类型
        modelAndView.addObject("modelType", modelType);
        // 在 controller 里处理
        modelAndView.setViewName("ftl/chat");
        return modelAndView;
    }
}

