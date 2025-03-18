package com.lijs.nex.auth.handler;

import com.lijs.nex.auth.constant.AuthConstant;
import com.lijs.nex.common.base.utils.JsonUtils;
import com.lijs.nex.common.base.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${spring.security.index-path}")
    private String index;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 方案1：返回 JSON 提示前端处理跳转
        // 设置相关cookie
        // CookieUtils.addCookie(response, "A", "false", AuthConstant.COOKIE_PATH, 0);
//        response.addHeader(AuthConstant.REDIRECT, index);
//        response.setContentType("application/json");
//        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.getWriter().write(Objects.requireNonNull(JsonUtils.toJson(ResultUtils.success(0))));

        // 目标服务地址，例如 index 服务
        String targetUrl = "http://127.0.0.1/web/chat";

        // 方案2：直接重定向
        // 获取当前应用的 contextPath (前缀例如 "/web")
        // String contextPath = request.getContextPath();
        // 登录成功后跳转到首页
        // response.sendRedirect(contextPath + "/auth/chat");
        response.sendRedirect(targetUrl);
    }


}
