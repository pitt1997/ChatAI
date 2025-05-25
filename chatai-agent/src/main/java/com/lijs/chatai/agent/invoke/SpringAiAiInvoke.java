package com.lijs.chatai.agent.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Spring AI 框架调用 AI 大模型（阿里）
 */
@Component
public class SpringAiAiInvoke implements CommandLineRunner {

    @Resource
//    private ChatModel openAiChatModel;
    private ChatModel deepseekChatModel;

    @Override
    public void run(String... args) throws Exception {
        AssistantMessage assistantMessage = deepseekChatModel.call(new Prompt("你好!"))
                .getResult()
                .getOutput();
        System.out.println(assistantMessage.getText());
    }
}