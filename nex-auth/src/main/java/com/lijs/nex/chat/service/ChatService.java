package com.lijs.nex.chat.service;

import com.lijs.nex.chat.llm.DeepSeekClient;
import com.lijs.nex.chat.llm.LocalLLMClient;
import com.lijs.nex.chat.llm.OpenAIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ljs
 * @date 2025-03-10
 * @description
 */
@Service
public class ChatService {

    @Autowired
    private OpenAIClient openAIClient;

    @Autowired
    private DeepSeekClient deepSeekClient;

    @Autowired
    private LocalLLMClient localLLMClient;

    public String callAIModel(String prompt, String modelType) {
        if ("chatgpt".equalsIgnoreCase(modelType)) {
            return openAIClient.chat(prompt);
        } else if ("deepseek".equalsIgnoreCase(modelType)) {
            return deepSeekClient.chat(prompt);
        } else if ("local".equalsIgnoreCase(modelType)) {
            return localLLMClient.chat(prompt);
        }
        return "Invalid Model Type";
    }
}
