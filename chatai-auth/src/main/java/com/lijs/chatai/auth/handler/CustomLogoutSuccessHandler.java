package com.lijs.chatai.auth.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 自定义退出成功逻辑 两种方式： 1、直接返回json 2、重定向到登录页面
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"code\":200, \"message\":\"退出成功\"}");
        // 重定向到登录页
//        String contextPath = request.getContextPath();
//        response.sendRedirect(contextPath + "/auth/login");
    }
}
