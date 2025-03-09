package com.lijs.fizz.auth.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 登录失败处理器
 *
 * @author du_jiao
 * @date 2022/3/10 17:38
 */

@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    //AuthenticationFailureHandler 只会处理 AuthenticationException 及其子类的异常，比如：
    //BadCredentialsException
    //DisabledException
    //LockedException
    //AccountExpiredException

    private final Logger logger = LoggerFactory.getLogger(CustomAuthFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 获取异常信息
        String errorMessage = exception.getMessage();

        // URL 编码，防止特殊字符导致重定向失败
        errorMessage = URLEncoder.encode(errorMessage, String.valueOf(StandardCharsets.UTF_8));

        // 获取当前应用的 contextPath (动态前缀，如 "/fizz-auth")
        String contextPath = request.getContextPath();  // 例如 "/fizz-auth"

        // 将错误信息存入 Session，避免 GET 方式带长参数
        request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", errorMessage);

        // 失败后重定向到登录页，并附带错误信息
        response.sendRedirect(contextPath + "/auth/login?error=" + URLEncoder.encode(exception.getMessage(), String.valueOf(StandardCharsets.UTF_8)));
    }
}
