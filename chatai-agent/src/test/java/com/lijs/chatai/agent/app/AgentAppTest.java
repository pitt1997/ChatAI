package com.lijs.chatai.agent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class AgentAppTest {

    @Resource
    private AgentApp agentApp;

    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是小李。";
        String answer = agentApp.doChatDeepSeek(message, chatId);
        // 第二轮
        message = "今天天气如何？";
        answer = agentApp.doChatDeepSeek(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "你知道叫什么来着？刚跟你说过。";
        answer = agentApp.doChatDeepSeek(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String message = "你好，我是小李，我想知道今天四川成都的天气。";
        AgentApp.WeatherReport weatherReport = agentApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(weatherReport);
    }


    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我正在谈恋爱，但是关系进展的还不太亲密，怎么办？";
        String answer = agentApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }
}
