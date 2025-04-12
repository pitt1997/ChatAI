package com.lijs.chatai.common.base.token;

import com.lijs.chatai.common.base.session.SessionUser;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ljs
 * @date 2024-10-28
 * @description
 */
@Component
public class JwtTokenProvider {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    /**
     * 密钥
     */
    @Value("${platform.jwt.secret:ChatAI@lijsELJjNVtOOTqx1srpUZ1OmQz0mFbIJWc0k}")
    private String secretKey;

    /**
     * token失效时间，默认60分钟
     */
    @Value("${platform.jwt.expiration:60}")
    private long expirationTime;

    public String generateToken(SessionUser sessionUser) {
        synchronized (JwtTokenProvider.class) { // 并发问题
            try {
                Map<String, Object> userAttrs = getUserAttrs(sessionUser);
                // 有效时间
                userAttrs.put(Claims.ISSUED_AT, new Date());
                userAttrs.put(Claims.EXPIRATION, new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(expirationTime)));
                return getBuilder()
                        .addClaims(userAttrs)
                        .compact();
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    public String generateToken(SessionUser sessionUser, long expire, TimeUnit timeUnit) {
        synchronized (JwtTokenProvider.class) { // 并发问题
            try {
                Map<String, Object> userAttrs = getUserAttrs(sessionUser);
                // 有效时间
                userAttrs.put(Claims.ISSUED_AT, new Date());
                userAttrs.put(Claims.EXPIRATION, new Date(System.currentTimeMillis() + timeUnit.toMillis(expire)));
                return getBuilder()
                        .addClaims(userAttrs)
                        .compact();
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    public SessionUser validateUserToken(String token) {
        Map<String, Object> data = getJwtParser().parseClaimsJws(token).getBody();
        SessionUser sessionUser = new SessionUser();
        data.forEach((key, value) -> {
            try {
                Field field = sessionUser.getClass().getDeclaredField(key);
                if (Modifier.isStatic(field.getModifiers())) { // 静态变量不处理
                    return;
                }
                field.setAccessible(true);
                field.set(sessionUser, value);
            } catch (NoSuchFieldException e) {
                logger.debug("field【`{}`】is not exist", key);
            } catch (IllegalAccessException e) {
                logger.error("field error -> ", e);
            }
        });
        return sessionUser;
    }

    public Claims validateToken(String token) {
        return getJwtParser()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return validateToken(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return validateToken(token).getExpiration().before(new Date());
    }

    private Map<String, Object> getUserAttrs(SessionUser sessionUser) throws IllegalAccessException {
        Map<String, Object> userAttrs = new HashMap<>();
        for (Field field : sessionUser.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            userAttrs.put(field.getName(), field.get(sessionUser));
        }
        return userAttrs;
    }

    private JwtBuilder getBuilder() {
        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
                        SignatureAlgorithm.HS256.getJcaName()));
    }

    private JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey((secretKey.getBytes()))
                .setAllowedClockSkewSeconds(1)
                .build();
    }

}