package com.lijs.chatai.agent.config;

import com.lijs.chatai.agent.rag.AppDocumentLoader;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 恋爱大师向量数据库配置（初始化基于内存的向量数据库 Bean）
 */
@Configuration
public class AppVectorStoreConfig {

    @Resource
    private AppDocumentLoader agentAppDocumentLoader;

    @Bean
    VectorStore agentAppVectorStore(EmbeddingModel deepseekEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(deepseekEmbeddingModel).build();
        List<Document> documentList = agentAppDocumentLoader.loadMarkdowns();
        simpleVectorStore.add(documentList);
        return simpleVectorStore;
    }
}
