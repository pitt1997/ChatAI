package com.lijs.chatai.common.base.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 提供一些常用的属性文件相关的方法
 */
public final class PropertiesUtils {

    public final static Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    /**
     * 从系统属性文件中获取相应的值
     *
     * @param key 属性的键
     * @return 属性的值
     */
    public static String getSystemProperty(String key) {
        return System.getProperty(key);
    }

    /**
     * 根据文件路径和Key读取对应的Value
     *
     * @param filePath 属性文件路径
     * @param key      需要读取的属性键
     * @return 属性的值
     */
    public static String getValueByKey(String filePath, String key) {
        try (InputStream in = new BufferedInputStream(new FileInputStream(filePath))) {
            Properties properties = new Properties();
            properties.load(in);
            return properties.getProperty(key);
        } catch (IOException e) {
            logger.error("Error reading property '{}': {}", key, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 加载所有属性到 Map 中
     *
     * @param in 输入流
     * @return 属性键值对的 Map
     */
    public static Map<String, String> loadProperties(InputStream in) {
        Map<String, String> map = new HashMap<>();
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            logger.error("Error loading properties: {}", e.getMessage(), e);
        }
        for (String key : properties.stringPropertyNames()) {
            map.put(key, properties.getProperty(key));
        }
        return map;
    }

    /**
     * 根据文件路径读取所有属性信息
     *
     * @param filePath 属性文件路径
     * @return 属性键值对的 Map
     */
    public static Map<String, String> getAllProperties(String filePath) {
        try (InputStream in = new BufferedInputStream(new FileInputStream(filePath))) {
            return loadProperties(in);
        } catch (IOException e) {
            logger.error("Error loading properties from file: {}", filePath, e);
            return new HashMap<>();
        }
    }

    /**
     * 写入或更新属性文件中的某个属性
     *
     * @param filePath 属性文件路径
     * @param key      属性键
     * @param value    属性值
     */
    public static void writeOrUpdateProperty(String filePath, String key, String value) {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(filePath)) {
            properties.load(in);
        } catch (IOException e) {
            logger.error("Error loading properties for update: {}", e.getMessage(), e);
        }

        try (FileOutputStream out = new FileOutputStream(filePath)) {
            properties.setProperty(key, value);
            properties.store(out, "Update '" + key + "' value");
        } catch (IOException e) {
            logger.error("Error writing property '{}': {}", key, e.getMessage(), e);
        }
    }

    /**
     * 更新属性文件中的指定属性（存在则更新，不存在则新增）
     *
     * @param filePath 属性文件路径
     * @param key      属性键
     * @param value    属性值
     */
    public static void updateProperty(String filePath, String key, String value) {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(filePath);
             FileOutputStream out = new FileOutputStream(filePath)) {
            properties.load(in);
            properties.setProperty(key, value);
            properties.store(out, null);
        } catch (IOException e) {
            logger.error("Error updating property '{}': {}", key, e.getMessage(), e);
        }
    }

}
