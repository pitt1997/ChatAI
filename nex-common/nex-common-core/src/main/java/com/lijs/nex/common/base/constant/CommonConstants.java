package com.lijs.nex.common.base.constant;

import java.time.format.DateTimeFormatter;

/**
 * 常用的常量
 */
public class CommonConstants {

    public interface Service {

        /**
         * 认证服务
         */
        String GATEWAY_SERVICE = "nex-gateway";

        /**
         * 认证服务
         */
        String AUTH_SERVICE = "nex-auth";

        /**
         * 管理服务
         */
        String ADMIN_SERVICE = "nex-admin";

        /**
         * 管理-用户服务
         */
        String USER_SERVICE = "nex-user";

        /**
         * 管理-用户服务
         */
        String ROLE_SERVICE = "nex-role-permission";

        /**
         * 管理-资源服务
         */
        String RESOURCE_SERVICE = "nex-resource";

    }

    public interface User {
        /**
         * 用户登录状态键
         */
        String USER_LOGIN_STATE = "userLoginState";

        /**
         * 默认权限
         */
        int DEFAULT_ROLE = 0;

        /**
         * 管理员权限
         */
        int ADMIN_ROLE = 1;
    }

    public interface Tenant {
        /**
         * 默认租户id为空
         */
        String DEFAULT_TENANT = "";

    }

    /**
     * 管理员信息
     */
    public interface ADMINISTRATOR {
        String DEFAULT_ID = "1";
        String DEFAULT_NAME = "admin";
        String DEFAULT_PASSWORD = "P@ssWord";
    }

    /**
     * 缓存 KEY
     */
    public interface CACHE_KEY {
        String LICENSE_INFO = "LICENSE_INFO";
    }

    /**
     * Topic
     */
    public interface TOPIC {

    }

    /**
     * DateTimeFormatter 时间格式
     */
    public interface DATE_FORMAT {
        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
        DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);
    }

    /**
     * 请求头
     */
    public interface HTTP_HEADER {

        /**
         * 请求来源
         */
        String REQUEST_FROM = "REQUEST-FROM";

        /**
         * 请求来源 INNER
         */
        String REQUEST_FROM_INNER = "INNER";

        /**
         * 请求开始时间
         */
        String REQUEST_START_TIME = "REQUEST-START-TIME";

        String BEARER = "Bearer";

        String BEARER_BLANK = "Bearer ";

        /**
         * cookie参数名 token
         */
        String TOKEN = "SESSION_TOKEN";

        String REDIRECT = "redirect";

        /**
         * 租户标识编码
         */
        String TENANT_ID = "TENANT_ID";

        /**
         * 租户标识编码
         */
        String TENANT = "TENANT";

        /**
         * feign标识请求头
         */
        String FEIGN_SIGN = "FEIGN-SIGN";

        String CLIENT_ID = "X-App-Client-ID";
        String RESPONSE_ENCODING = "X-App-Response-Encoding";
        String UNIQUE_REQUEST_ID = "X-App-Request-ID";
        String CONTENT_TYPE = "Content-Type";
        String DATA_ENCRYPT = "X-App-Data-Encrypt";
        String TIMESTAMP = "X-App-Timestamp";
        String SIGNATURE = "X-App-Signature";
        String API_KEY = "X-App-API-Key";
    }

    /**
     * uri常量
     */
    public interface URI {
        String LICENSE_IMPORT = "/license/import";
        String USER_LOGIN = "/user/login";
    }

    /**
     * 日期时间类型格式
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期类型格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 日期类型格式
     */
    public static final String YEAR_MONTH_FORMAT = "yyyyMM";

    /**
     * 日期类型格式
     */
    public static final String YEAR_MONTH_DAY_FORMAT = "yyyyMMdd";

    /**
     * 时间类型的格式
     */
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * 空字符串
     */
    public static final String STR_EMPTY = "";

    /**
     * JVM版本
     */
    public static final String JVM_VERSION = "java.version";

    /**
     * JVM编码
     */
    public static final String JVM_ENCODING = "file.encoding";

    /**
     * JVM默认临时目录
     */
    public static final String JVM_TEMPDIR = "java.io.tmpdir";

    /**
     * 代理主机Host
     */
    public static final String HTTP_PROXY_HOST = "http.proxyHost";

    /**
     * 代理主机端口
     */
    public static final String HTTP_PROXY_PORT = "http.proxyPort";

    /**
     * 代理用户
     */
    public static final String HTTP_PROXY_USER = "http.proxyUser";

    /**
     * 代理用户密码
     */
    public static final String HTTP_PROXY_PASSWORD = "http.proxyPassword";

    /**
     * 主机架构
     */
    public static final String SYS_OS_ARCH = "os.arch";

    /**
     * 主机类型
     */
    public static final String SYS_OS_NAME = "os.name";

    /**
     * 主机类型版本
     */
    public static final String SYS_OS_VERSION = "os.version";

    /**
     * 操作系统类型
     */
    public static final String SYS_SUN_DESKTOP = "sun.desktop";

    /**
     * 系统文件分隔符
     */
    public static final String SYS_FILE_SEPARATOR = "file.separator";

    /**
     * 系统路径分隔符
     */
    public static final String SYS_PATH_SEPARATOR = "path.separator";

    /**
     * 系统换行符
     */
    public static final String SYS_LINE_SEPARATOR = "line.separator";

}
