package com.lijs.fizz.common.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则校验工具类
 */
public final class RegexUtils {

    private RegexUtils() {
        // 工具类不应实例化
    }

    /**
     * 封装邮箱、电话等正则校验
     * 使用示例：
     * <pre>{@code
     * boolean isEmailValid = RegexUtils.EMAIL_PATTERN.matcher("test@example.com").matches();
     * }</pre>
     */
    public static final String REG_EMAIL = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final Pattern EMAIL_PATTERN = Pattern.compile(REG_EMAIL);

    /**
     * 电话号码
     */
    public static final String REG_FIXED_TELEPHONE = "^\\d{3,4}-?\\d{7,8}$";
    public static final Pattern TELEPHONE_PATTERN = Pattern.compile(REG_FIXED_TELEPHONE);

    /**
     * 身份证编码
     */
    public static final String REG_IDENTIFICATION_CARD = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|X)$";
    public static final Pattern IDENTIFICATION_CARD_PATTERN = Pattern.compile(REG_IDENTIFICATION_CARD);

    /**
     * URL地址
     */
    public static final String REG_URL = "^(http|https|ftp)://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,4}(:[0-9]+)?(/[a-zA-Z0-9\\-\\._\\?,'/\\\\+&%\\$#=~]*)?$";
    public static final Pattern URL_PATTERN = Pattern.compile(REG_URL);

    /**
     * 手机号码
     */
    public static final String REG_MOBILE_TELEPHONE = "^(1[3-9])\\d{9}$";
    public static final Pattern MOBILE_TELEPHONE_PATTERN = Pattern.compile(REG_MOBILE_TELEPHONE);

    /**
     * 特殊字符
     */
    public static final String CONTAINS_SPECIAL_CHAR = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
    public static final Pattern CONTAINS_SPECIAL_CHAR_PATTERN = Pattern.compile(CONTAINS_SPECIAL_CHAR);

    /**
     * IP地址（IPv4或IPv6）
     */
    public static final String REG_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(?!$)|$)){4}";
    public static final Pattern IP_PATTERN = Pattern.compile(REG_IP);

    /**
     * IPv4 地址
     */
    public static final String REG_IPV4 = "^(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}$";
    public static final Pattern IPV4_PATTERN = Pattern.compile(REG_IPV4);

    /**
     * IPv6 地址
     */
    public static final String REG_IPV6 = "([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}";
    public static final Pattern IPV6_PATTERN = Pattern.compile(REG_IPV6);

    /**
     * MAC 地址
     */
    public static final String REG_MAC = "^([0-9A-Fa-f]{2}-){5}([0-9A-Fa-f]{2})$";
    public static final Pattern MAC_PATTERN = Pattern.compile(REG_MAC);

    /**
     * 端口
     */
    public static final String REG_PORT = "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
    public static final Pattern PORT_PATTERN = Pattern.compile(REG_PORT);

    /**
     * 中文名称
     */
    public static final String REG_CHINESE_NAME = "^[\u4e00-\u9fa5]{2,4}$";
    public static final Pattern CHINESE_NAME_PATTERN = Pattern.compile(REG_CHINESE_NAME);

    /**
     * FTP服务器地址
     */
    public static final String REG_FTP_ADDRESS = "^(ftp)://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,4}(:[0-9]+)?(/[a-zA-Z0-9\\-\\._\\?,'/\\\\+&%\\$#=~]*)?$";
    public static final Pattern FTP_ADDRESS_PATTERN = Pattern.compile(REG_FTP_ADDRESS);

    /**
     * 域名
     */
    public static final String REG_DOMAIN = "^(?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.(?!-)[A-Za-z0-9-]{1,63}(?<!-)$";
    public static final Pattern DOMAIN_PATTERN = Pattern.compile(REG_DOMAIN);

    /**
     * 日期（年月日）
     */
    public static final String REG_DATE = "^\\d{4}-\\d{2}-\\d{2}$";
    public static final Pattern DATE_PATTERN = Pattern.compile(REG_DATE);

    /**
     * 时间（时分秒）
     */
    public static final String REG_TIME = "^([01]?[0-9]|2[0-3]):[0-5]?[0-9](:[0-5]?[0-9])?$";
    public static final Pattern TIME_PATTERN = Pattern.compile(REG_TIME);

    /**
     * 日期时间（年月日 时分秒）
     */
    public static final String REG_DATE_TIME = "^\\d{4}-\\d{2}-\\d{2} ([01]?[0-9]|2[0-3]):[0-5]?[0-9](:[0-5]?[0-9])?$";
    public static final Pattern DATE_TIME_PATTERN = Pattern.compile(REG_DATE_TIME);

    /**
     * UUID
     */
    public static final String REG_UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final Pattern UUID_PATTERN = Pattern.compile(REG_UUID);

    /**
     * 判断字符串是否符合给定的正则表达式
     *
     * @param str   待检查的字符串
     * @param regex 正则表达式
     * @return 是否匹配
     */
    public static boolean matches(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

}
