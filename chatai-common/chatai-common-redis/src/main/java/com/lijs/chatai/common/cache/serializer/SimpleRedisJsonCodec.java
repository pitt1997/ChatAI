package com.lijs.chatai.common.cache.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * 禁用序列化存储类的类型信息的部分
 */
public class SimpleRedisJsonCodec extends RedisJsonCodec {

    public SimpleRedisJsonCodec() {
        super();
        ObjectMapper mapper = getObjectMapper();
        customizeObjectMapper(mapper);
    }

    public SimpleRedisJsonCodec(ClassLoader classLoader) {
        super(classLoader);
        ObjectMapper mapper = getObjectMapper();
        customizeObjectMapper(mapper);
    }

    public SimpleRedisJsonCodec(ClassLoader classLoader, SimpleRedisJsonCodec codec) {
        super(classLoader, codec);
        ObjectMapper mapper = getObjectMapper();
        customizeObjectMapper(mapper);
    }

    public SimpleRedisJsonCodec(ObjectMapper mapper) {
        super(mapper);
        customizeObjectMapper(mapper);
    }

    /**
     * 禁用类型信息，并注册 JavaTimeModule 模块
     */
    private void customizeObjectMapper(ObjectMapper mapper) {
        // 禁用类的类型信息
        mapper.deactivateDefaultTyping();
        mapper.registerModule(new JavaTimeModule());
    }

}
