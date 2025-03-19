package com.lijs.nex.auth.handler;

import com.lijs.nex.auth.constant.AuthConstant;
import com.lijs.nex.common.base.session.SessionUser;
import com.lijs.nex.common.base.token.JwtTokenProvider;
import com.lijs.nex.common.base.utils.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomAuthSuccessHandler.class);

    @Value("${spring.security.index-path}")
    private String index;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 方式1: 重定向到首页
        // 获取当前应用的 contextPath (前缀例如 "/web")
        String contextPath = request.getContextPath();
        String redirectURL = contextPath + index;
        logger.info("登录成功，跳转至首页:{}", redirectURL);

        UsernamePasswordAuthenticationToken authenticationUser = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        SessionUser sessionUser = new SessionUser();
        sessionUser.setUsername(authenticationUser.getName());
        sessionUser.setUserId(authenticationUser.getName());
        // 生成 Token（假设你用 JWT）
        String token = jwtTokenProvider.generateToken( sessionUser);
        response.setHeader("Authorization", "Bearer " + token);
        CookieUtils.addCookie(response, AuthConstant.COOKIE_TOKEN, token, AuthConstant.COOKIE_PATH, AuthConstant.COOKIE_EXPIRE); // 过期时间 1 小时
        response.sendRedirect(redirectURL);

        // 方式2: 直接返回json前端解析自行跳转
//        // 设置相关cookie
//        // CookieUtils.addCookie(response, "AC", "false", AuthConstant.COOKIE_PATH, 0);
//        response.addHeader(AuthConstant.REDIRECT, redirectURL);
//        response.setContentType("application/json");
//        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.getWriter().write(Objects.requireNonNull(JsonUtils.toJson(ResultUtils.success(0))));
    }

}
