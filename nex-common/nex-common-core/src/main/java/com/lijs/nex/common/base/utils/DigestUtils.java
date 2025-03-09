package com.lijs.nex.common.base.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.bouncycastle.crypto.digests.SM3Digest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 摘要工具类 md5摘要
 */
public class DigestUtils {

    private static final String MD5_ALGORITHM_NAME = "MD5";

    private static final char[] HEX_CHARS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 将密钥存储在配置文件中
     */
    private final static HmacUtils hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_MD5, System.getenv("HMAC_KEY"));

    /**
     * MD5摘要字符串（32位大写）
     *
     * @param string 需要进行MD5加密的字符串
     * @return 摘要字符串（大写）
     */
    public static String digest32UpperCase(String string) {
        char[] hexDigest = digestAsHexChars(string.getBytes());
        return new String(hexDigest).toUpperCase();
    }

    /**
     * MD5摘要字符串（32位小写）
     *
     * @param string 需要进行MD5加密的字符串
     * @return 摘要字符串（大写）
     */
    public static String digest32LowerCase(String string) {
        char[] hexDigest = digestAsHexChars(string.getBytes());
        return new String(hexDigest).toLowerCase();
    }

    /**
     * MD5摘要字符串（16位大写）
     *
     * @param string 需要进行MD5加密的字符串
     * @return 摘要字符串（大写）
     */
    public static String digest16UpperCase(String string) {
        return digest32UpperCase(string).substring(8, 24);
    }

    /**
     * MD5摘要字符串（16位小写）
     *
     * @param string 需要进行MD5加密的字符串
     * @return 摘要字符串（大写）
     */
    public static String digest16LowerCase(String string) {
        return digest32LowerCase(string).substring(8, 24);
    }

    private static char[] digestAsHexChars(byte[] bytes) {
        byte[] digest = getDigest(MD5_ALGORITHM_NAME).digest(bytes);
        return encodeHex(digest);
    }

    private static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + "\"", ex);
        }
    }

    private static char[] encodeHex(byte[] bytes) {
        char[] chars = new char[32];
        for (int i = 0; i < chars.length; i = i + 2) {
            byte b = bytes[i / 2];
            chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
            chars[i + 1] = HEX_CHARS[b & 0xf];
        }
        return chars;
    }

    /**
     * 返回形式为数字跟字符串
     * @param bytes
     * @return
     */
    private static String byteToArrayString(byte bytes) {
        int iRet = bytes;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    /**
     * 转换字节数组为16进制字串
     */
    private static String byteToString(byte[] bytes) {
        StringBuilder sBuffer = new StringBuilder();
        for (byte b : bytes) {
            sBuffer.append(byteToArrayString(b));
        }
        return sBuffer.toString();
    }

    public static String getMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = strObj;
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    /**
     * 获取 HmacUtils 实例
     *
     * @param algorithm Hmac 算法类型（例如 HMAC_MD5, HMAC_SHA_256 等）
     * @return HmacUtils 实例
     */
    private static HmacUtils getHmacUtils(HmacAlgorithms algorithm) {
        String key = System.getenv("HMAC_KEY");  // 从环境变量中获取密钥
        if (key == null || key.isEmpty()) {
            // throw new IllegalArgumentException("HMAC 密钥未设置或为空");
            key = "HMAC_KEY";
        }
        return new HmacUtils(algorithm, key);
    }

    /**
     * 使用 HMAC-MD5 进行加密
     *
     * @param plainText 要加密的文本
     * @return 加密后的文本
     */
    public static String hmacMd5Hex(String plainText) {
        return getHmacUtils(HmacAlgorithms.HMAC_MD5).hmacHex(plainText);
    }

    /**
     * 使用 HMAC-SHA-1 进行加密
     *
     * @param plainText 要加密的文本
     * @return 加密后的文本
     */
    public static String hmacShaHex(String plainText) {
        return getHmacUtils(HmacAlgorithms.HMAC_SHA_1).hmacHex(plainText);
    }

    /**
     * MD2 加密摘要
     *
     * @param text 待加密的文本
     * @return MD2 哈希值
     */
    public static String md2(String text) {
        return org.apache.commons.codec.digest.DigestUtils.md2Hex(text);
    }

    /**
     * MD5 加密摘要
     *
     * @param text 待加密的文本
     * @return MD5 哈希值
     */
    public static String md5(String text) {
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(text);
    }

    /**
     * SHA-1 加密摘要
     *
     * @param text 待加密的文本
     * @return SHA-1 哈希值
     */
    public static String sha1(String text) {
        return org.apache.commons.codec.digest.DigestUtils.sha1Hex(text);
    }

    /**
     * SHA-256 加密摘要
     *
     * @param text 待加密的文本
     * @return SHA-256 哈希值
     */
    public static String sha256(String text) {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(text);
    }

    /**
     * SHA-384 加密摘要
     *
     * @param text 待加密的文本
     * @return SHA-384 哈希值
     */
    public static String sha384(String text) {
        return org.apache.commons.codec.digest.DigestUtils.sha384Hex(text);
    }

    /**
     * SHA-512 加密摘要
     *
     * @param text 待加密的文本
     * @return SHA-512 哈希值
     */
    public static String sha512(String text) {
        return org.apache.commons.codec.digest.DigestUtils.sha512Hex(text);
    }

    /**
     * SHA3-224 加密摘要
     *
     * @param text 待加密的文本
     * @return SHA3-224 哈希值
     */
    public static String sha3_224(String text) {
        return org.apache.commons.codec.digest.DigestUtils.sha3_224Hex(text);
    }

    /**
     * SHA3-256 加密摘要
     *
     * @param text 待加密的文本
     * @return SHA3-256 哈希值
     */
    public static String sha3_256(String text) {
        return org.apache.commons.codec.digest.DigestUtils.sha3_256Hex(text);
    }

    /**
     * SHA3-384 加密摘要
     *
     * @param text 待加密的文本
     * @return SHA3-384 哈希值
     */
    public static String sha3_384(String text) {
        return org.apache.commons.codec.digest.DigestUtils.sha3_384Hex(text);
    }

    /**
     * SHA3-512 加密摘要
     *
     * @param text 待加密的文本
     * @return SHA3-512 哈希值
     */
    public static String sha3_512(String text) {
        return org.apache.commons.codec.digest.DigestUtils.sha3_512Hex(text);
    }

    /**
     * SM3 加密摘要 - 中国国家标准算法
     *
     * @param text 待加密的文本
     * @return SM3 哈希值
     */
    public static String sm3(String text) {
        SM3Digest digest = new SM3Digest();
        byte[] inputBytes = text.getBytes();
        digest.update(inputBytes, 0, inputBytes.length);
        byte[] output = new byte[digest.getDigestSize()];
        digest.doFinal(output, 0);
        return Hex.encodeHexString(output);
    }

    /**
     * HMAC-MD5 加密摘要
     *
     * @param text 待加密的文本
     * @param key  HMAC 密钥
     * @return HMAC-MD5 哈希值
     */
    public static String hmacMd5(String text, String key) {
        return new HmacUtils(HmacAlgorithms.HMAC_MD5, key).hmacHex(text);
    }

    /**
     * HMAC-SHA1 加密摘要
     *
     * @param text 待加密的文本
     * @param key  HMAC 密钥
     * @return HMAC-SHA1 哈希值
     */
    public static String hmacSha1(String text, String key) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key).hmacHex(text);
    }

    public static void main(String[] args) {
        System.out.println(DigestUtils.getMD5Code("123456"));
    }

}
