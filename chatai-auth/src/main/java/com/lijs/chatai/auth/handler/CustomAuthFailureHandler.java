package com.lijs.chatai.auth.handler;

import com.lijs.chatai.common.base.enums.ErrorCodeEnum;
import com.lijs.chatai.common.base.utils.JsonUtils;
import com.lijs.chatai.common.base.utils.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 登录失败处理器
 * AuthenticationFailureHandler 只会处理 AuthenticationException 及其子类的异常，比如：
 * BadCredentialsException
 * DisabledException
 * LockedException
 * AccountExpiredException
 */
@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomAuthFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String error = exception.getMessage();
        logger.error("登录失败...{}", error);
        handleJson(request, response, error);
    }

    /**
     * 重定向跳转方式
     */
    public void handleRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取当前应用的 contextPath (动态前缀，如 "/web")
        String contextPath = request.getContextPath();
        // 失败后重定向到登录页，并附带错误信息
        response.sendRedirect(contextPath + "/auth/login?error=password error");
    }

    /**
     * 前后端分离场景：直接返回json
     */
    public void handleJson(HttpServletRequest request, HttpServletResponse response, String error) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(Objects.requireNonNull(JsonUtils.toJson(ResultUtils.error(ErrorCodeEnum.PARAMS_ERROR, error, error))));
    }
}
