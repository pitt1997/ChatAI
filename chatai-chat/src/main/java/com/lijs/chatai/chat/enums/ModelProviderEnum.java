package com.lijs.chatai.chat.enums;

/**
 * @author ljs
 * @date 2025-04-16
 * @description
 */
public enum ModelProviderEnum {
    OPENAI("OPENAI", "OPENAI"),
    GEMINI("GEMINI", "Google"),
    WENXINYIYAN("WENXINYIYAN", "Google"),
    CLAUDE("CLAUDE", "Google"),
    QIANWEN("QIANWEN", "通义千问"),
    DEEPSEEK("DEEPSEEK", "DEEPSEEK");

    private final String code;

    private final String name;

    ModelProviderEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}