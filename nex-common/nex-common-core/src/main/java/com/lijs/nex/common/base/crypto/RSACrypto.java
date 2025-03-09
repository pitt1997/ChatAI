package com.lijs.nex.common.base.crypto;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author ljs
 * @date 2024-10-30
 * @description RSA非对称加解密
 */
public class RSACrypto {

    private static final String RSA = "RSA";
    private static final int RSA_KEY_SIZE = 2048;

    /**
     * 内置密钥对
     */
    private static final KeyPair KEY_PAIR = generateKeyPair();

    private RSACrypto() {
    }

    /**
     * 使用内置公钥加密数据
     */
    public static String encrypt(String data) {
        return encrypt(data, KEY_PAIR.getPublic());
    }

    /**
     * 使用内置私钥解密数据
     */
    public static String decrypt(String encryptedData) {
        return decrypt(encryptedData, KEY_PAIR.getPrivate());
    }

    /**
     * 使用给定的公钥加密数据
     */
    public static String encrypt(String data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("RSA encryption failed", e);
        }
    }

    /**
     * 使用给定的私钥解密数据
     */
    public static String decrypt(String encryptedData, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            return new String(cipher.doFinal(decodedBytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("RSA decryption failed", e);
        }
    }

    /**
     * 使用字符串密钥进行加密
     */
    public static String encryptWithStringKey(String data, String publicKeyStr) {
        try {
            PublicKey publicKey = KeyFactory.getInstance(RSA)
                    .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr)));
            return encrypt(data, publicKey);
        } catch (Exception e) {
            throw new RuntimeException("Encryption with string key failed", e);
        }
    }

    /**
     * 使用字符串密钥进行解密
     */
    public static String decryptWithStringKey(String encryptedData, String privateKeyStr) {
        try {
            PrivateKey privateKey = KeyFactory.getInstance(RSA)
                    .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr)));
            return decrypt(encryptedData, privateKey);
        } catch (Exception e) {
            throw new RuntimeException("Decryption with string key failed", e);
        }
    }

    /**
     * 生成RSA密钥对
     *
     * @return
     */
    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(RSA);
            keyGen.initialize(RSA_KEY_SIZE);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("RSA key pair generation failed", e);
        }
    }

    public static void main(String[] args) {
        String originalData = "Hello, RSA Encryption!";

        // 使用内置密钥进行加密和解密
        String encryptedData = RSACrypto.encrypt(originalData);
        System.out.println("Encrypted with built-in key: " + encryptedData);

        String decryptedData = RSACrypto.decrypt(encryptedData);
        System.out.println("Decrypted with built-in key: " + decryptedData);

        // 获取密钥对的字符串表示
        String publicKeyStr = Base64.getEncoder().encodeToString(KEY_PAIR.getPublic().getEncoded());
        String privateKeyStr = Base64.getEncoder().encodeToString(KEY_PAIR.getPrivate().getEncoded());

        // 使用字符串密钥进行加密和解密
        String encryptedWithStringKey = RSACrypto.encryptWithStringKey(originalData, publicKeyStr);
        System.out.println("Encrypted with string key: " + encryptedWithStringKey);

        String decryptedWithStringKey = RSACrypto.decryptWithStringKey(encryptedWithStringKey, privateKeyStr);
        System.out.println("Decrypted with string key: " + decryptedWithStringKey);
    }

}
