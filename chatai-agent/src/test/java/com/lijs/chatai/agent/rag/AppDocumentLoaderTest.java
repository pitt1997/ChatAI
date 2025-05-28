package com.lijs.chatai.agent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppDocumentLoaderTest {

    @Resource
    private AppDocumentLoader agentAppDocumentLoader;

    @Test
    void loadMarkdowns() {
        agentAppDocumentLoader.loadMarkdowns();
    }
}