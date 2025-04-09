package com.lijs.chatai.common.base.utils;

import com.lijs.chatai.common.base.crypto.AESCrypto;
import com.lijs.chatai.common.base.crypto.RSACrypto;

/**
 * @author ljs
 * @date 2024-10-30
 * @description 加解密工具类
 */
public class CryptoUtils {

    /**
     * 对称加密方法，默认使用 AES
     *
     * @param data 需要加密的数据
     * @return 加密后的字符串
     */
    public static String encrypt(String data) {
        return AESCrypto.encrypt(data);
    }

    /**
     * 非对称加密方法
     *
     * @param data 需要加密的数据
     * @return 加密后的字符串
     */
    public static String encryptAsymmetric(String data) {
        return RSACrypto.encrypt(data);
    }

    /**
     * 解密方法，默认使用 AES
     *
     * @param data 需要解密的数据
     * @return 解密后的字符串
     */
    public static String decrypt(String data) {
        return AESCrypto.decrypt(data);
    }

    /**
     * 非对称解密方法
     *
     * @param data 需要解密的数据
     * @return 解密后的字符串
     */
    public static String decryptAsymmetric(String data) {
        return RSACrypto.decrypt(data);
    }

}
