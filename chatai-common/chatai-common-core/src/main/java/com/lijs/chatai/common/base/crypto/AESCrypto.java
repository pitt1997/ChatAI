package com.lijs.chatai.common.base.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESCrypto {

    public static final String AES = "AES";
    public static final String AES_CBC_PADDING = "AES/CBC/PKCS5Padding";
    private static final String DEFAULT_SECRET_KEY = "chatai@20241030!P@ssWord";
    private static final int AES_KEY_SIZE = 256;
    private static final int IV_SIZE = 16;

    public AESCrypto() {
    }

    /**
     * 加密方法
     */
    public static String encrypt(String data) {
        return encrypt(data, DEFAULT_SECRET_KEY, false, false);
    }

    /**
     * 解密方法
     */
    public static String decrypt(String data) {
        return decrypt(data, DEFAULT_SECRET_KEY, false, false);
    }

    /**
     * 加密方法，支持动态生成密钥和初始化向量
     *
     * @param data      待加密数据
     * @param key       密钥字符串
     * @param dynamicIv 是否动态生成IV
     * @param useBase64 是否使用Base64编码
     * @return 加密后的字符串
     */
    public static String encrypt(String data, String key, boolean dynamicIv, boolean useBase64) {
        try {
            byte[] iv = dynamicIv ? generateRandomIV() : fixedIV();
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            SecretKeySpec secretKey = new SecretKeySpec(getMD5Hash(key), AES);
            Cipher cipher = Cipher.getInstance(AES_CBC_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            byte[] ivAndCipherText = dynamicIv ? concatenate(iv, encrypted) : encrypted;

            return useBase64 ? Base64.getEncoder().encodeToString(ivAndCipherText) : byteArr2HexStr(ivAndCipherText);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密方法
     *
     * @param encryptedData Base64编码的密文
     * @param key           密钥字符串
     * @param dynamicIv     是否使用动态IV
     * @param useBase64     是否接受Base64编码的输入
     * @return 解密后的原文
     */
    public static String decrypt(String encryptedData, String key, boolean dynamicIv, boolean useBase64) {
        try {
            byte[] decodedData = useBase64 ? Base64.getDecoder().decode(encryptedData) : hexStr2ByteArr(encryptedData);

            byte[] iv = dynamicIv ? extractIV(decodedData) : fixedIV();
            byte[] cipherText = dynamicIv ? extractCipherText(decodedData) : decodedData;

            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKey = new SecretKeySpec(getMD5Hash(key), AES);
            Cipher cipher = Cipher.getInstance(AES_CBC_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成固定IV（用于不使用动态IV的情况）
     *
     * @return 固定IV字节数组
     */
    private static byte[] fixedIV() {
        return new byte[]{29, -23, 46, 121, 69, -91, -88, -5, 120, 40, 22, -39, 47, -32, 24, -108};
    }

    /**
     * 生成随机IV
     *
     * @return 随机IV字节数组
     */
    private static byte[] generateRandomIV() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    /**
     * 生成MD5哈希密钥
     *
     * @param key 密钥字符串
     * @return 哈希后的密钥字节数组
     */
    private static byte[] getMD5Hash(String key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return md5.digest(key.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5算法不可用", e);
        }
    }

    /**
     * 拼接IV和密文
     */
    private static byte[] concatenate(byte[] iv, byte[] cipherText) {
        byte[] combined = new byte[IV_SIZE + cipherText.length];
        System.arraycopy(iv, 0, combined, 0, IV_SIZE);
        System.arraycopy(cipherText, 0, combined, IV_SIZE, cipherText.length);
        return combined;
    }

    /**
     * 从加密数据中提取IV
     */
    private static byte[] extractIV(byte[] data) {
        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(data, 0, iv, 0, IV_SIZE);
        return iv;
    }

    /**
     * 从加密数据中提取密文
     */
    private static byte[] extractCipherText(byte[] data) {
        byte[] cipherText = new byte[data.length - IV_SIZE];
        System.arraycopy(data, IV_SIZE, cipherText, 0, cipherText.length);
        return cipherText;
    }

    public static String byteArr2HexStr(byte[] buf) {
        int iLen = buf.length;
        StringBuilder sb = new StringBuilder(iLen * 2);
        for (byte b : buf) {
            int intTmp;
            intTmp = b;
            while (intTmp < 0) {
                intTmp += 256;
            }
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    public static byte[] hexStr2ByteArr(String hexStr) {
        byte[] arrB = hexStr.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[iLen / 2];

        for (int i = 0; i < iLen; i += 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }

        return arrOut;
    }

    public static void main(String[] args) {
        System.out.println(AESCrypto.encrypt("123456"));
        System.out.println(AESCrypto.encrypt("123456"));
        System.out.println(AESCrypto.encrypt("666666"));
        String s = "053842aa63a87d24a45b88c606bc254a";
        System.out.println(AESCrypto.decrypt(s));
        System.out.println(1);
    }

}
