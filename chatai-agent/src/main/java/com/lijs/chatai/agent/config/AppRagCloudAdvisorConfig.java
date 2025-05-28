package com.lijs.chatai.agent.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义基于阿里云知识库服务的 RAG 增强顾问
 */
//@Configuration
//@Slf4j
//public class AppRagCloudAdvisorConfig {
//
//    @Value("${spring.ai.openai.api-key}")
//    private String deepSeekApiKey;
//
//    @Bean
//    public Advisor loveAppRagCloudAdvisor() {
//        DashScopeApi dashScopeApi = new DashScopeApi(deepSeekApiKey);
//        final String KNOWLEDGE_INDEX = "恋爱大师";
//        DocumentRetriever dashScopeDocumentRetriever = new DashScopeDocumentRetriever(dashScopeApi,
//                DashScopeDocumentRetrieverOptions.builder()
//                        .withIndexName(KNOWLEDGE_INDEX)
//                        .build());
//        return RetrievalAugmentationAdvisor.builder()
//                .documentRetriever(dashScopeDocumentRetriever)
//                .build();
//    }
//}
