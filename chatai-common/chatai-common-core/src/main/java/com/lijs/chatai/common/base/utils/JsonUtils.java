package com.lijs.chatai.common.base.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectMapper SORT_MAPPER = new ObjectMapper();
    private static final ObjectMapper NOT_NULL_MAP_VALUES_MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.registerModule(new JavaTimeModule());
    }

    public static String toJson(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            logger.error("JSON转换失败", e);
        }
        return null;
    }

    public static <T> T toObject(String jsonData, Class<T> valueType) {
        try {
            return MAPPER.readValue(jsonData, valueType);
        } catch (Exception e) {
            logger.error("JSON反序列化失败", e);
        }
        return null;
    }

    public static <T> T toObject(JsonNode jsonNode, Class<T> beanType) {
        try {
            return MAPPER.readValue(MAPPER.writeValueAsString(jsonNode), beanType);
        } catch (Exception e) {
            logger.error("JSON反序列化失败", e);
        }
        return null;
    }

    /**
     * 转换为泛型类
     *
     * @param objectType   目标类型
     * @param genericsType 泛型实参类型
     */
    public static <O, G> O toGenericsObject(String jsonData, Class<O> objectType, Class<G> genericsType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(objectType, genericsType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 反序列化泛型对象
     *
     * @param jsonData      需要反序列的数据
     * @param typeReference 泛型类型引用
     */
    public static <T> T toGenericsObject(String jsonData, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(jsonData, typeReference);
        } catch (Exception e) {
            logger.error("泛型反序列化失败", e);
        }
        return null;
    }

    public static <T> List<T> toList(String jsonData, Class<T> valueType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, valueType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static <K, V> Map<K, V> toMap(String jsonData, Class<K> keyType, Class<V> valueType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(HashMap.class, keyType, valueType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static Map toMap(String jsonData) {
        try {
            return MAPPER.readValue(jsonData, Map.class);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T mapToObject(Map<Object, Object> map, Class<T> clazz) {
        try {
            return MAPPER.convertValue(map, clazz);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T mapToObject(Map<Object, Object> map, TypeReference<T> clazz) {
        try {
            return MAPPER.convertValue(map, clazz);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> List<T> toList(List<T> data, Class<T> tClass) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, tClass);
        try {
            return MAPPER.readValue(MAPPER.writeValueAsString(data), javaType);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 通过JSONPath直接获取JSON中对应值
     *
     * @param json     json字符串 eg. "{ \"data\": { \"user\": \"John\" } }"
     * @param jsonPath JSONPath eg. data.user
     * @return 获取对应path路径的json字段 eg. John
     * @throws IOException
     */
    public static String evaluate(String json, String jsonPath) throws IOException {
        JsonNode rootNode = MAPPER.readTree(json);
        return evaluate(rootNode, jsonPath);
    }

    public static String evaluate(JsonNode jsonNode, String jsonPath) {
        String[] segments = jsonPath.split("\\.");
        for (String segment : segments) {
            if (jsonNode.isObject()) {
                jsonNode = jsonNode.get(segment);
            } else if (jsonNode.isArray()) {
                try {
                    int index = Integer.parseInt(segment);
                    jsonNode = jsonNode.get(index);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid array index: " + segment);
                }
            } else {
                throw new IllegalArgumentException("Invalid JSONPath: " + jsonPath);
            }
            if (jsonNode == null) {
                return null;
            }
        }
        if (jsonNode.isTextual()) {
            return jsonNode.asText();
        } else {
            return jsonNode.toString();
        }
    }

    /**
     * 获取按照对象的属性值进行排序后的json
     * 替换fastjson中对应功能 JSONObject.toJSONString(request, SerializerFeature.SortField);
     */
    public static String toSortByKeyJson(Object data) {
        // 配置 ObjectMapper 以按字段名排序
        SORT_MAPPER.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        // 将对象序列化为 JSON 字符串并按字段名排序
        try {
            return SORT_MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

}
