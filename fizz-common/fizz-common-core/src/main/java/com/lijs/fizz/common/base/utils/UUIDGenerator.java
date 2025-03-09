package com.lijs.fizz.common.base.utils;

import java.util.UUID;

public class UUIDGenerator {

    public synchronized static String get32UUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 返回16字符长度的UUID字符串
     *
     * @return 16字符UUID
     */
    public static String uuid16() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }

    /**
     * 返回小写UUID字符串（无连字符）32位
     *
     * @return 小写UUID
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 返回大写UUID字符串（无连字符）
     *
     * @return 大写UUID
     */
    public static String upperCaseUUID() {
        return uuid().toUpperCase();
    }

    /**
     * 生成包含主机特征码的UUID编码
     *
     * @param hostFeature 主机特征码
     * @return UUID编码
     */
    public static String sequencedUUID(String hostFeature) {
        long timestamp = System.currentTimeMillis();
        String baseUuid = UUID.randomUUID().toString().replaceAll("-", "");
        return Long.toHexString(timestamp) + hostFeature + baseUuid.substring(0, 16 - hostFeature.length());
    }

}

