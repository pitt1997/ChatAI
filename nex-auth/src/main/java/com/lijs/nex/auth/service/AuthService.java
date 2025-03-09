package com.lijs.nex.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijs.nex.auth.constant.AuthConstant;
import com.lijs.nex.common.base.constant.CommonConstants;
import com.lijs.nex.common.base.enums.ErrorCodeEnum;
import com.lijs.nex.common.base.session.SessionUser;
import com.lijs.nex.common.base.tenant.TenantContextHolder;
import com.lijs.nex.common.base.token.JwtTokenProvider;
import com.lijs.nex.common.base.utils.ResultUtils;
import com.lijs.nex.common.base.utils.TokenUtils;
import feign.FeignException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public static final String USER_TOKEN_PREFIX = "user-token:";

    private final JwtTokenProvider tokenProvider;
    private final ValueOperations<String, String> opsForValue;
    RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.security.index-path}")
    private String indexPath;

    public AuthService(JwtTokenProvider tokenProvider,
                       RedisTemplate<String, String> redisTemplate,
                       ObjectMapper objectMapper) {
        this.tokenProvider = tokenProvider;
        opsForValue = redisTemplate.opsForValue();
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void auth(HttpServletRequest request, HttpServletResponse response, SessionUser user) throws IOException {
        String userToken = opsForValue.get(getTokenKey(user));

        if (userToken != null) {
            authUrlWithToken(userToken, user.getUsername(), request, response);
            return;
        }

        SessionUser sessionUser = generateUserEntity(user);
        userToken = tokenProvider.generateToken(sessionUser);
        opsForValue.set(getTokenKey(user), userToken, 50L, TimeUnit.MINUTES);
        authUrlWithToken(userToken, user.getUsername(), request, response);
    }

    private static String getTokenKey(SessionUser user) {
        String tenant = TenantContextHolder.get();
        if (StringUtils.isNotBlank(tenant)) {
            return USER_TOKEN_PREFIX + tenant + ":" + user.getUsername();
        }
        return USER_TOKEN_PREFIX + user.getUsername();
    }

    public void authUrlWithToken(String token, String userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String authUrl = getAuthUrl(request);
        String method = request.getHeader(AuthConstant.X_FORWARDED_METHOD_HEADER);
        if (StringUtils.isBlank(authUrl)) {
            response.sendRedirect(indexPath);
            return;
        }

        if (StringUtils.isBlank(method)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendRedirect(indexPath);
            return;
        }


        try {
            // URL 鉴权 鉴权请求之前给 feign 添加
            // FeignUserHelper.setUserToken(token);
            response.setHeader(HttpHeaders.AUTHORIZATION, TokenUtils.TOKEN_PREFIX + token);
            String feignAuthSign = request.getHeader(CommonConstants.HTTP_HEADER.FEIGN_SIGN);
            if (StringUtils.isNotEmpty(feignAuthSign)) {
                if (feignAuthSign.length() == 32) {
                    return;
                }

            }
            // 权限接口查询
//            if (handleResult == null) {
//                response.getWriter().write(objectMapper.writeValueAsString(ResultUtils.error(ErrorCodeEnum.NO_AUTH)));
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                response.getWriter().write(objectMapper.writeValueAsString(ResultUtils.error(ErrorCodeEnum.NO_AUTH)));
//            }
            logger.debug("用户 id `{}` 访问 url `{}`", userId, authUrl);
        } catch (FeignException.Forbidden e) {
            logger.info("用户 id `{}` 没有权限访问 url `{}`", userId, authUrl);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(objectMapper.writeValueAsString(ResultUtils.error(ErrorCodeEnum.NO_AUTH)));
        } catch (FeignException.ServiceUnavailable e) {
            logger.info("系统服务不可用");
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write(objectMapper.writeValueAsString(ResultUtils.error(ErrorCodeEnum.NO_AUTH)));
        } catch (FeignException e) {
            logger.error("", e);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(objectMapper
                    .writeValueAsString(ResultUtils.error(ErrorCodeEnum.NO_AUTH)));
        }
    }

    public String getAuthUrl(HttpServletRequest request) {
        return getPath(request.getHeader(AuthConstant.X_FORWARDED_URI_HEADER));
    }

    public String getPath(String uri) {
        if (StringUtils.isBlank(uri)) {
            return null;
        }
        uri = uri.replaceFirst("/\\?", "?");
        if (uri.contains("?")) {
            return uri.substring(0, uri.indexOf("?"));
        }
        if (uri.endsWith("/")) {
            return uri.substring(0, uri.length() - 1);
        }
        return uri;
    }

    public String getParamFromUrl(String url, String param) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        String regex = "(?<=[?&])" + param + "=([^&]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 有些特殊的uri 从鉴权的参数中获取租户
     */
    private final static Pattern TENANT_PATTERN = Pattern.compile("(?<=[?&])" + "tenant" + "=([^&]*)");

    public static String getParamTenant(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        Matcher matcher = TENANT_PATTERN.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public SessionUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken casAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        return (SessionUser) casAuthenticationToken.getPrincipal();
    }

    private SessionUser generateUserEntity(SessionUser user) {
        SessionUser userEntity = new SessionUser();
        userEntity.setUserId(user.getUserId());
        userEntity.setUsername(user.getUsername());
        return userEntity;
    }

    public void clearToken(Authentication authentication) {
        UsernamePasswordAuthenticationToken casAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        SessionUser user = (SessionUser) casAuthenticationToken.getPrincipal();
        // 获取在线用户的sessionId
        Set<String> keys = redisTemplate.keys("auth-server:session:sessions:expires:*");
        Object[] arr = {"sessionAttr:SPRING_SECURITY_CONTEXT"};
        ObjectMapper objectMapper = new ObjectMapper();
        int onlineUserCount = 0;
        for (String key : keys) {
            // 解析在线用户
            String sessionkey = "auth-server:session:sessions:" + key.substring(37);
            List<Object> values = redisTemplate.opsForHash().multiGet(sessionkey, Arrays.asList(arr));
            SessionUser onlineUser = new SessionUser();
            JsonNode obj = null;
            try {
                obj = objectMapper.readTree((String) values.get(0));
                JsonNode principal = obj.get("authentication").get("principal");
                onlineUser.setUserId(principal.get("userId").asText());
                onlineUser.setTenantId(principal.get("tenant").asText());
            } catch (Exception e) {
                logger.error("在线用户解析失败", e);
                continue;
            }
            // 判断是否存在和退出用户一样的在线用户，此时正在退出的用户的sessionId还未删除，本身就会存在1个
            if (user.getTenantId() == null) {
                if (user.getUserId().equals(onlineUser.getUserId())) {
                    onlineUserCount++;
                }
            } else {
                if (user.getTenantId().equals(onlineUser.getTenantId()) && user.getUserId().equals(onlineUser.getUserId())) {
                    onlineUserCount++;
                }
            }
            if (onlineUserCount > 1) {
                return;
            }
        }
        opsForValue.set(getTokenKey(user), "", 10L, TimeUnit.MICROSECONDS);
    }
}
