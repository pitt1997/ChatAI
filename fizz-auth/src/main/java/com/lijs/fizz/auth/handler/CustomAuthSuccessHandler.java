package com.lijs.fizz.auth.handler;

import com.lijs.fizz.auth.constant.AuthConstant;
import com.lijs.fizz.common.base.utils.JsonUtils;
import com.lijs.fizz.common.base.utils.ResultUtils;
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

    // json 格式返回
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 设置相关cookie
        // CookieUtils.addCookie(response, "A", "false", AuthConstant.COOKIE_PATH, 0);
        response.addHeader(AuthConstant.REDIRECT, index);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(Objects.requireNonNull(JsonUtils.toJson(ResultUtils.success(0))));


        // 获取当前应用的 contextPath (动态前缀，如 "/fizz-auth")
        String contextPath = request.getContextPath();  // 例如 "/fizz-auth"
        // 失败后重定向到登录页，并附带错误信息
        response.sendRedirect(contextPath + "/auth/login?error=");


    }


}
