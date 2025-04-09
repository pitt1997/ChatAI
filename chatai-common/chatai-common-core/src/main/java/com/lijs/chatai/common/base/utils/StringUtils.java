package com.lijs.chatai.common.base.utils;

import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关操作的工具类
 */
public final class StringUtils {

    /**
     * 将字符串数组或列表用给定的符号连接成一个字符串
     *
     * @param array  需要处理的字符串数组
     * @param symbol 连接的符号
     * @return 处理后的字符串
     */
    public static String join(String[] array, String symbol) {
        StringBuilder sb = new StringBuilder();
        if (array != null) {
            for (String temp : array) {
                if (temp != null && temp.trim().length() > 0)
                    sb.append(temp).append(symbol);
            }
            if (sb.length() > 1 && CheckUtils.isNotEmpty(symbol)) {
                sb = new StringBuilder(sb.substring(0, sb.length() - symbol.length()));
            }
        }
        return sb.toString();
    }

    /**
     * 将字符串列表用给定的符号连接成一个字符串
     *
     * @param list   需要处理的字符串集合
     * @param symbol 连接的符号
     * @return 处理后的字符串
     */
    public static String join(List<String> list, String symbol) {
        StringBuilder result = new StringBuilder();
        if (list != null) {
            for (String temp : list) {
                if (temp != null && temp.trim().length() > 0)
                    result.append(temp).append(symbol);
            }
            if (result.length() > 1 && CheckUtils.isNotEmpty(symbol)) {
                result = new StringBuilder(result.substring(0, result.length() - symbol.length()));
            }
        }
        return result.toString();
    }

    /**
     * 数字格式化
     *
     * @param value  传入的小数
     * @param format 保留小数格式
     * @return 格式化后的字符串
     */
    public static String formatNumber(BigDecimal value, String format) {
        DecimalFormat df = new DecimalFormat(format);
        if (value.compareTo(BigDecimal.ZERO) > 0 && value.compareTo(new BigDecimal(1)) < 0) {
            return "0" + df.format(value);
        } else {
            return df.format(value);
        }
    }

    /**
     * 判断字符串是否非空
     *
     * @param str 字符串
     * @return 是否非空
     */
    public static boolean hasLength(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * 字符串替换
     *
     * @param input      输入字符串
     * @param oldPattern 需要替换的子字符串
     * @param newPattern 替换后的子字符串
     * @return 替换后的字符串
     */
    public static String replace(String input, String oldPattern, String newPattern) {
        if (hasLength(input) && hasLength(oldPattern) && newPattern != null) {
            int index = input.indexOf(oldPattern);
            if (index == -1) {
                return input;
            } else {
                int capacity = input.length();
                if (newPattern.length() > oldPattern.length()) {
                    capacity += 16;
                }
                StringBuilder sb = new StringBuilder(capacity);
                int pos = 0;
                for (int patLen = oldPattern.length(); index >= 0; index = input.indexOf(oldPattern, pos)) {
                    sb.append(input, pos, index);
                    sb.append(newPattern);
                    pos = index + patLen;
                }
                sb.append(input.substring(pos));
                return sb.toString();
            }
        } else {
            return input;
        }
    }

    /**
     * 判断是否是空字符串，包括空对象和空值
     *
     * @param str 判断的字符串
     * @return 是否有效
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串不为空
     *
     * @param str 判断的字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * 字符串省略截取
     * 字符串截取到指定长度size+...的形式
     *
     * @param subject 需要处理的字符串
     * @param size    截取的长度
     * @return 处理后的字符串
     */
    public static String subStringOmit(String subject, int size) {
        if (subject != null && subject.length() > size) {
            subject = subject.substring(0, size) + "...";
        }
        return subject;
    }

    /**
     * 隐藏邮箱前缀
     *
     * @param email 邮箱 例如: 12345@qq.com
     * @return 返回邮箱前缀隐藏 eg. *****@qq.com.
     */
    public static String hideEmailPrefix(String email) {
        if (email != null) {
            int index = email.lastIndexOf('@');
            if (index > 0) {
                email = repeat("*", index).concat(email.substring(index));
            }
        }
        return email;
    }

    /**
     * 通过源字符串重复生成N次组成新的字符串。
     *
     * @param src - 源字符串 例如: (" "), ("*")
     * @param num - 重复生成次数
     * @return 返回已生成的重复字符串
     */
    public static String repeat(String src, int num) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < num; i++)
            s.append(src);
        return s.toString();
    }

    /**
     * 页面中去除字符串中的空格、回车、换行符、制表符
     *
     * @param str 需要处理的字符串
     */
    public static String removeWhitespace(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }

    /**
     * 替换第一个出现的子串
     *
     * @param s    字符串
     * @param sub  出现值
     * @param with 替换目标值
     */
    public static String replaceFirst(String s, String sub, String with) {
        int i = s.indexOf(sub);
        if (i == -1) {
            return s;
        }
        return s.substring(0, i) + with + s.substring(i + sub.length());
    }

    /**
     * 替换最后一次出现的字串
     *
     * @param s    字符串
     * @param sub  出现值
     * @param with 替换目标值
     */
    public static String replaceLast(String s, String sub, String with) {
        int i = s.lastIndexOf(sub);
        if (i == -1) {
            return s;
        }
        return s.substring(0, i) + with + s.substring(i + sub.length());
    }

    /**
     * 将字符串首字母转大写
     *
     * @param str 需要处理的字符串
     */
    public static String upperFirstChar(String str) {
        if (isEmpty(str)) {
            return "";
        }
        char[] cs = str.toCharArray();
        if ((cs[0] >= 'a') && (cs[0] <= 'z')) {
            cs[0] -= (char) 0x20;
        }
        return String.valueOf(cs);
    }

    /**
     * 将字符串首字母转小写
     *
     * @param str 需要处理的字符串
     */
    public static String lowerFirstChar(String str) {
        if (isEmpty(str)) {
            return "";
        }
        char[] cs = str.toCharArray();
        if ((cs[0] >= 'A') && (cs[0] <= 'Z')) {
            cs[0] += (char) 0x20;
        }
        return String.valueOf(cs);
    }

    /**
     * 编码字符串
     *
     * @param str     字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 编码后的字节码
     */
    public static byte[] bytes(CharSequence str, Charset charset) {
        if (str == null) {
            return null;
        }
        if (null == charset) {
            return str.toString().getBytes();
        }
        return str.toString().getBytes(charset);
    }

    /**
     * 解码字节码
     *
     * @param data    字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public static String str(byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }
        if (charset == null) {
            return new String(data);
        }
        return new String(data, charset);
    }

    /**
     * 字符串是否以给定字符开始
     *
     * @param str 字符串
     * @param c   字符
     * @return 是否开始
     */
    public static boolean startWith(CharSequence str, char c) {
        return c == str.charAt(0);
    }

    /**
     * 将给定的字符序列转换为 StringReader 对象。
     *
     * @param str 输入的字符序列，例如 String 或 StringBuilder 对象
     * @return 如果输入不为 null，返回对应的 StringReader；否则返回 null
     */
    public static StringReader getReader(CharSequence str) {
        if (null == str) {
            return null;
        }
        return new StringReader(str.toString());
    }

    /**
     * 判断字符串是否为空，null，空格，多个空格都视为空
     *
     * @return true 为空，false 不为空
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为空，null，空格，多个空格都视为空
     *
     * @return true 不为空，false 为空
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * {@link CharSequence} 转为字符串，null安全
     *
     * @param cs {@link CharSequence}
     * @return 字符串
     */
    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data    数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String str(ByteBuffer data, Charset charset) {
        if (null == charset) {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }

    /**
     * 判断字符串是否含大写
     **/
    public static boolean isHasUpperCase(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 97 && c <= 122) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否含小写
     **/
    public static boolean isHasLowerCase(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 65 && c <= 90) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否含数字
     **/
    public static boolean isHasNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 48 && c <= 57) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否含输入字符
     **/
    public static boolean isHasSpecialChar(String str, String special) {
        for (int i = 0; i < special.length(); i++) {
            char c = special.charAt(i);
            if (str.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将包含非字母数字字符的字符串分割为数组，过滤掉空字符串元素
     *
     * @param str 输入的字符串
     * @return 分割后的字符串数组
     */
    public static String[] arrStr2arr(String str) {
        String[] strArray = str.split("[^\\w\\d]");
        List<String> list = new ArrayList<>();
        for (String s : strArray) {
            if (s != null && !s.isEmpty()) {
                list.add(s);
            }
        }
        return list.toArray(new String[1]);
    }

    /**
     * 替换字符串模板中的占位符
     * 模板格式：使用 {index} 表示占位符，例如 "Hello, {0}!"
     *
     * @param template 字符串模板
     * @param args     替换的参数
     * @return 替换后的字符串
     * @throws NullPointerException 若 template 为 null
     */
    public static String replaceTemplate(String template, Object... args) {
        if (template == null) {
            throw new NullPointerException("template is null");
        }
        if (args == null || args.length == 0) {
            return template;
        }
        for (int i = 0; i < args.length; i++) {
            template = template.replaceAll("\\{" + i + "}", Matcher.quoteReplacement(String.valueOf(args[i])));
        }
        return template;
    }

    /**
     * 将 Base64 编码的字符串转换为字节数组
     *
     * @param base64Str Base64 编码的字符串
     * @return 解码后的字节数组，若解码失败则返回空数组
     */
    public static byte[] toBytes(String base64Str) {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] decoderBytes = new byte[0];
        try {
            decoderBytes = decoder.decodeBuffer(base64Str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return decoderBytes;
    }

}
