package com.lijs.chatai.auth.filter;

import com.lijs.chatai.auth.constant.AuthConstant;
import com.lijs.chatai.common.base.session.SessionUser;
import com.lijs.chatai.common.base.token.JwtTokenProvider;
import com.lijs.chatai.common.base.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 认证过滤器（JwtAuthenticationFilter）
 * <p>
 * 该过滤器用于拦截所有 HTTP 请求，从请求头或 Cookie 中获取 JWT Token，
 * 并校验 Token 的有效性。如果 Token 合法，则解析用户信息并将其存入
 * Spring Security 的上下文中，实现身份认证。
 * <p>
 * 主要功能：
 * 1. 从请求头 `Authorization: Bearer <TOKEN>` 或 Cookie 获取 Auth-Token。
 * 2. 通过 `JwtTokenProvider.validateUserToken()` 校验 Token 是否有效。
 * 3. 如果 Token 有效，解析用户信息，并创建 Spring Security 认证对象 `Authentication`。
 * 4. 将认证信息存入 `SecurityContextHolder`，确保后续请求能够识别用户身份。
 * 5. 继续执行过滤链，允许请求访问后续资源。
 * <p>
 * 适用于：
 * - 保护需要身份认证的 API 端点，确保用户已登录。
 * - 与 Spring Security 结合，实现基于 Token 的身份认证。
 *
 * @author ljs
 * @date 2025-02-11
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (StringUtils.isNotEmpty(token)) {
            SessionUser sessionUser = jwtTokenProvider.validateUserToken(token);
            if (sessionUser != null) {
                String username = sessionUser.getUsername();
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        // 请求头中获取
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        // Query 参数中获取（WebSocket 常用）
        String webSocketToken = request.getParameter("WebSocket-Authorization");
        if (webSocketToken != null) {
            return webSocketToken;
        }

        // 直接cookie中获取
        return CookieUtils.getCookie(request, AuthConstant.COOKIE_TOKEN);
    }
}