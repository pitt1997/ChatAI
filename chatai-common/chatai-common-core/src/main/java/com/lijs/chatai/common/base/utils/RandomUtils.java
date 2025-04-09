package com.lijs.chatai.common.base.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * 随机生成字符串、数值工具类
 */
public final class RandomUtils {

    public static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LETTER_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBER_CHAR = "0123456789";

    /**
     * 生成指定范围内的随机整数
     *
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 随机整数
     */
    public static int integer(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    /**
     * 返回指定长度的随机数字字符串
     *
     * @param length 字符串长度
     * @return 随机数字字符串
     */
    public static String numberString(int length) {
        return randomStringFromChars(NUMBER_CHAR, length);
    }

    /**
     * 返回一个定长的随机字符串(包含大小写字母、数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String string(int length) {
        return randomStringFromChars(ALL_CHAR, length);
    }

    /**
     * 返回一个定长的随机纯字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String letterString(int length) {
        return randomStringFromChars(LETTER_CHAR, length);
    }

    /**
     * 返回一个定长的随机纯大写字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String lowerLetterString(int length) {
        return letterString(length).toLowerCase();
    }

    /**
     * 返回一个定长的随机纯小写字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String upperLetterString(int length) {
        return letterString(length).toUpperCase();
    }

    /**
     * 生成指定长度的全'0'字符串
     *
     * @param length 字符串长度
     * @return 全0字符串
     */
    public static String zeroString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append('0');
        }
        return sb.toString();
    }

    /**
     * 根据数字生成指定长度的字符串，不足位数前面补0
     *
     * @param number    数字
     * @param totalLength 字符串长度
     * @return 指定长度的字符串
     */
    public static String paddedNumberString(long number, int totalLength) {
        String strNum = String.valueOf(number);
        if (strNum.length() > totalLength) {
            throw new IllegalArgumentException("数字 " + number + " 超出指定长度 " + totalLength);
        }
        return zeroString(totalLength - strNum.length()) + strNum;
    }

    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     *
     * @param number       数字
     * @param totalLength 字符串长度
     * @return 定长的字符串
     */
    public static String paddedNumberString(int number, int totalLength) {
        StringBuilder sb = new StringBuilder();
        String strNum = String.valueOf(number);
        if (totalLength - strNum.length() >= 0) {
            sb.append(zeroString(totalLength - strNum.length()));
        } else {
            throw new RuntimeException("将数字" +
                    number + "转化为长度为" + totalLength + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }

    /**
     * 从数组中随机取出指定数量的元素，确保没有重复
     *
     * @param array 元素数组
     * @param count 随机元素数量
     * @return 不重复的随机元素组合
     */
    public static int[] uniqueRandomInts(int[] array, int count) {
        int[] copy = Arrays.copyOf(array, array.length);
        Collections.shuffle(Arrays.asList(copy));
        return Arrays.copyOf(copy, count);
    }

    /**
     * 每次生成的len位数都不相同
     *
     * @param array
     * @return 定长的数字
     */
    public static int getNotSimple(int[] array, int len) {
        Random rand = new Random();
        for (int i = array.length; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < len; i++) {
            result = result * 10 + array[i];
        }
        return result;
    }

    /**
     * 从指定的数组中随机返回一个元素
     *
     * @param array 数组
     * @param <T>   数组元素类型
     * @return 随机元素
     */
    public static <T> T randomItem(T[] array) {
        return array[integer(0, array.length - 1)];
    }

    public static String uuid16() {
        return UUIDGenerator.uuid16();
    }

    /**
     * 返回一个UUID
     *
     * @return 小写的UUID
     */
    public static String uuid() {
        return UUIDGenerator.uuid();
    }

    /**
     * 返回一个UUID
     *
     * @return 大写的UUID
     */
    public static String UUID() {
        return UUIDGenerator.upperCaseUUID();
    }

    /**
     * 返回一个有序列的uuid编码
     * 前11位为时间(毫秒)
     * 中间4位为主机特征码
     * 剩下的保证其唯一性
     *
     * @param hostFeature 主机特征码建议设置4位
     * @return UUID编码
     */
    public static String squid(String hostFeature) {
        return UUIDGenerator.sequencedUUID(hostFeature);
    }

    /**
     * 从数组中根据权重比例随机选择元素
     *
     * @param array     数组
     * @param weights   权重比例数组
     * @param <T>       数组元素类型
     * @return 随机选择的元素
     */
    public static <T> T randomItemByWeight(T[] array, double[] weights) {
        if (array.length != weights.length) {
            throw new IllegalArgumentException("数组和权重数组长度不一致");
        }
        double totalWeight = Arrays.stream(weights).sum();
        double randValue = Math.random() * totalWeight;
        double cumulativeWeight = 0.0;
        for (int i = 0; i < array.length; i++) {
            cumulativeWeight += weights[i];
            if (randValue < cumulativeWeight) {
                return array[i];
            }
        }
        throw new RuntimeException("权重随机选择失败");
    }

    /**
     * 从指定字符集生成指定长度的随机字符串
     *
     * @param charset 字符集
     * @param length  长度
     * @return 随机字符串
     */
    private static String randomStringFromChars(String charset, int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(charset.charAt(random.nextInt(charset.length())));
        }
        return sb.toString();
    }

}
