package com.lijs.chatai.auth.constant;

/**
 * @author ljs
 * @date 2025-03-06
 * @description
 */
public class AuthConstant {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String AUTH_URL = "/auth";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String REDIRECT = "redirect";

    public static final String X_FORWARDED_URI_HEADER = "x-forwarded-uri";
    public static final String X_FORWARDED_METHOD_HEADER = "x-forwarded-method";

    public static final String HOME_INDEX = "/index";

    public static final String COOKIE_PATH = "/";

    public static final String COOKIE_TOKEN = "AUTH-TOKEN";

    public static final String COOKIE_JSESSIONID = "JSESSIONID";

    /**
     * 过期时间 1小时
     */
    public static final int COOKIE_EXPIRE = 60 * 60;

}
