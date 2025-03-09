package com.lijs.nex.common.base.session;

import com.lijs.nex.common.base.token.TokenHolder;

/**
 * 用户上下文工具类
 */
public class SessionUserHelper {

    /**
     * 超级管理员
     */
    public static final String SUPER_ADMINISTRATOR_ID = "1";
    public static final String SUPER_ADMINISTRATOR_NAME = "admin";

    /**
     * 获取用户
     */
    public static SessionUser getUser() {
        return SessionUserHolder.get();
    }

    /***
     * 获取用户ID
     */
    public static String getUserId() {
        return getUser() != null ? getUser().getUserId() : "";
    }

    /**
     * 获取用户信息，如果为null则返回默认用户信息
     */
    public static SessionUser getOrCreateDefaultUser() {
        SessionUser sessionUser = SessionUserHolder.get();
        if (sessionUser != null) {
            return sessionUser;
        }
        return new SessionUser(SUPER_ADMINISTRATOR_NAME, SUPER_ADMINISTRATOR_ID);
    }

    public static SessionUser getUserOrThrow() {
        SessionUser sessionUser = SessionUserHolder.get();
        if (sessionUser != null) {
            return sessionUser;
        }
        throw new RuntimeException("当前线程【" + Thread.currentThread() + "】获取用户失败");
    }

    public static void setUser(SessionUser sessionUserInfo) {
        SessionUserHolder.set(sessionUserInfo);
    }

    public static void clearUser() {
        SessionUserHolder.remove();
    }

    public static String getUserToken() {
        return TokenHolder.get();
    }

    public static void setUserToken(String token) {
        TokenHolder.set(token);
    }

    public static void clearUserToken() {
        TokenHolder.clear();
    }

}
