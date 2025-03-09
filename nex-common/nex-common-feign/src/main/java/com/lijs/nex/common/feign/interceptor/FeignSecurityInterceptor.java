package com.lijs.nex.common.feign.interceptor;

import com.google.common.net.HttpHeaders;
import com.lijs.nex.common.base.constant.CommonConstants;
import com.lijs.nex.common.base.token.JwtTokenProvider;
import com.lijs.nex.common.base.utils.TokenUtils;
import com.lijs.nex.common.base.token.TokenHolder;
import com.lijs.nex.common.base.tenant.TenantContextHolder;
import com.lijs.nex.common.base.session.SessionUser;
import com.lijs.nex.common.base.session.SessionUserHelper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class FeignSecurityInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(FeignSecurityInterceptor.class);

    private static final String SECRET_KEY = "SecretKey123!";

    private final JwtTokenProvider jwtTokenProvider;
    private static volatile String globalToken;
    private static volatile LocalDateTime expire;

    public FeignSecurityInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 统一拦截请求进行权限校验等操作
     */
    @Override
    public void apply(RequestTemplate template) {
        // 请求是否已经处理过
        Collection<String> authorization = template.headers().get(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.size() > 0) {
            return;
        }

        // 设置票据到请求头中
        String jwt = retrieveToken();
        template.header(HttpHeaders.AUTHORIZATION, Collections.singletonList(TokenUtils.TOKEN_PREFIX + jwt));

        if (!(template.headers().containsKey("Content-Type") || template.method().equals("GET"))) {
            template.header(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
        }

        // 设置租户上下文到请求头中
        if (StringUtils.isNotEmpty(TenantContextHolder.get())) {
            template.header("TD", TenantContextHolder.get());
        }

        String localeLanguage = LocaleContextHolder.getLocale().toString();
        logger.debug("feign 调用语言: {}", localeLanguage);

        // 设置feign调用加密标识头设置
        String md5Code = DigestUtils.md5Hex(jwt + SECRET_KEY);
        template.header(CommonConstants.HTTP_HEADER.FEIGN_SIGN, md5Code);
    }

    private String retrieveToken() {
        String token = TokenHolder.get();
        if (StringUtils.isEmpty(token)) {
            token = SessionUserHelper.getUserToken();
        }
        if (StringUtils.isEmpty(token)) {
            token = retrieveOrRefreshGlobalToken();
        }
        if (StringUtils.isEmpty(token)) {
            throw new RuntimeException("当前线程中获取用户票据失败");
        }
        return token;
    }

    private String retrieveOrRefreshGlobalToken() {
        if (globalToken == null) {
            synchronized (FeignSecurityInterceptor.class) {
                if (globalToken == null) {
                    // 首次生成
                    generateGlobalToken();
                }
            }
        }
        if (Duration.between(LocalDateTime.now(), expire).toMinutes() < 2L) {
            synchronized (FeignSecurityInterceptor.class) {
                if (Duration.between(LocalDateTime.now(), expire).toMinutes() < 2L) {
                    // 即将过期时重新生成
                    generateGlobalToken();
                }
            }
        }
        return globalToken;
    }

    private void generateGlobalToken() {
        SessionUser sessionUser = new SessionUser();
        sessionUser.setUserId(CommonConstants.ADMINISTRATOR.DEFAULT_ID);
        sessionUser.setUsername(CommonConstants.ADMINISTRATOR.DEFAULT_NAME);
        globalToken = jwtTokenProvider.generateToken(sessionUser);
        expire = LocalDateTime.now();
    }
}
