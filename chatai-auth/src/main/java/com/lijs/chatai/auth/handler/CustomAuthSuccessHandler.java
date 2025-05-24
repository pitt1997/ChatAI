package com.lijs.chatai.auth.handler;

import com.lijs.chatai.auth.constant.AuthConstant;
import com.lijs.chatai.common.base.session.SessionUser;
import com.lijs.chatai.common.base.token.JwtTokenProvider;
import com.lijs.chatai.common.base.utils.CookieUtils;
import com.lijs.chatai.common.base.utils.JsonUtils;
import com.lijs.chatai.common.base.utils.ResultUtils;
import com.lijs.chatai.common.cache.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomAuthSuccessHandler.class);

    private final static String USER_TOKEN = "UserToken-%s";

    @Value("${spring.security.index-path}")
    private String index;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Resource
    private CacheService cacheService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Default behavior
        handleJsonSuccess(request, response, authentication);
    }

    /**
     * 重定向跳转方式
     */
    public void handleRedirectSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String contextPath = request.getContextPath();
        String redirectURL = contextPath + index;

        logger.info("登录成功，跳转至首页:{}", redirectURL);

        UsernamePasswordAuthenticationToken authenticationUser = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        SessionUser sessionUser = new SessionUser();
        sessionUser.setUsername(authenticationUser.getName());
        sessionUser.setUserId(authenticationUser.getName());

        String token = jwtTokenProvider.generateToken(sessionUser);
        response.setHeader("Authorization", "Bearer " + token);
        CookieUtils.addCookie(response, AuthConstant.COOKIE_TOKEN, token, AuthConstant.COOKIE_PATH, AuthConstant.COOKIE_EXPIRE);
        cacheService.setWithTTL(USER_TOKEN + token, sessionUser, 60 * 60 * 1000);

        response.sendRedirect(redirectURL);
    }

    /**
     * 前后端分离场景：直接返回json
     */
    public void handleJsonSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        UsernamePasswordAuthenticationToken authenticationUser = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        SessionUser sessionUser = (SessionUser) authenticationUser.getPrincipal();

        String token = jwtTokenProvider.generateToken(sessionUser);
        response.setHeader("Authorization", "Bearer " + token);
        CookieUtils.addCookie(response, AuthConstant.COOKIE_TOKEN, token, AuthConstant.COOKIE_PATH, AuthConstant.COOKIE_EXPIRE);
        cacheService.setWithTTL(USER_TOKEN + token, sessionUser, 60 * 60 * 1000);

        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(Objects.requireNonNull(JsonUtils.toJson(ResultUtils.success(token))));
    }

}
