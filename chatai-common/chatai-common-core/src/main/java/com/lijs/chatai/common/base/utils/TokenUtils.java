package com.lijs.chatai.common.base.utils;

import org.springframework.http.HttpHeaders;

import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenUtils {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String REDIRECT_HEADER = "redirect";
    public static final int TOKEN_SUBSTRING_BEGIN_INDEX = TOKEN_PREFIX.length();

    public static final String INVALID_TOKEN = createErrorMessage("invalid_token", "The access token invalid");
    public static final String INVALID_EXPIRED_TOKEN = createErrorMessage("invalid_token", "The access token expired");
    public static final String INVALID_NO_TENANT_TOKEN = createErrorMessage("invalid_tenant", "NO tenant info");
    public static final String INVALID_LOGOUT_TOKEN_TOKEN = createErrorMessage("invalid_token", "Logout token");
    public static final String INVALID_HAVE_NOT_TOKEN = createErrorMessage("invalid_token", "Have not token");
    public static final String INVALID_FEIGN_TOKEN = createErrorMessage("invalid_token", "Feign access token invalid");

    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return null;
        }
        return token.substring(TOKEN_SUBSTRING_BEGIN_INDEX);
    }

    public static void sendUnauthorized(ServletResponse response, String tokenErrorInfo) throws IOException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setHeader(HttpHeaders.WWW_AUTHENTICATE, tokenErrorInfo);
        res.getWriter().write(tokenErrorInfo);
    }

    public static void sendUnauthorizedAndRedirect(ServletResponse response, String tokenErrorInfo, String uri) throws IOException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        res.setHeader(HttpHeaders.WWW_AUTHENTICATE, tokenErrorInfo);
        res.setHeader(REDIRECT_HEADER, uri);
        res.getWriter().write(tokenErrorInfo);
    }

    private static String createErrorMessage(String error, String description) {
        return TOKEN_PREFIX + "error=\"" + error + "\", error_description=\"" + description + "\"";
    }
}
