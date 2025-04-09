package com.lijs.chatai.auth.handler;

import com.lijs.chatai.auth.constant.AuthConstant;
import com.lijs.chatai.common.base.utils.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ljs
 * @date 2025-03-14
 * @description
 */
@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomLogoutHandler.class);

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 清除 cookie
        CookieUtils.addCookie(response, AuthConstant.COOKIE_TOKEN, "", AuthConstant.COOKIE_PATH, AuthConstant.COOKIE_EXPIRE); // 过期时间 1 小时
        CookieUtils.addCookie(response, AuthConstant.COOKIE_JSESSIONID, "", AuthConstant.COOKIE_PATH, AuthConstant.COOKIE_EXPIRE); // 过期时间 1 小时
        logger.info("退出成功, cookie 清除完成!");
    }
}