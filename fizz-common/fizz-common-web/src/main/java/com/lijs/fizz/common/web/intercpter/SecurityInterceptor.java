package com.lijs.fizz.common.web.intercpter;

import com.lijs.fizz.common.cache.service.RedisCacheService;
import com.lijs.fizz.common.base.constant.CommonConstants;
import com.lijs.fizz.common.base.token.JwtTokenProvider;
import com.lijs.fizz.common.base.utils.TokenUtils;
import com.lijs.fizz.common.base.request.RequestContext;
import com.lijs.fizz.common.base.token.TokenHolder;
import com.lijs.fizz.common.base.request.RequestHelper;
import com.lijs.fizz.common.base.tenant.TenantContextHolder;
import com.lijs.fizz.common.base.session.SessionUser;
import com.lijs.fizz.common.base.session.SessionUserHelper;
import com.lijs.fizz.common.mybatis.config.DataSourceRouting;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SecurityInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    private static final String SECRET_KEY = "SecretKey123!";

    private final static String USER_TOKEN = "UserToken-%s";

    private final List<String> anonUrls = new CopyOnWriteArrayList<>();

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private RedisCacheService cacheService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private DataSourceRouting dataSourceRouting;

    public SecurityInterceptor(DataSourceRouting dataSourceRouting, String... anonUrls) {
        // 数据源
        this.dataSourceRouting = dataSourceRouting;
        if (anonUrls != null) {
            this.anonUrls.addAll(Arrays.stream(anonUrls).collect(Collectors.toSet()));
        }
        this.anonUrls.add("/error");
        this.anonUrls.add("/admin/user/getUserByName");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        // 租户标识
        String tenantId = request.getHeader(CommonConstants.HTTP_HEADER.TENANT_ID);
        if (StringUtils.isNotEmpty(tenantId)) {
            // 拦截设置租户上下文标识
            TenantContextHolder.set(tenantId);
            if (!dataSourceRouting.getResolvedDataSources().containsKey(tenantId)) {
                logger.error("tenant [{}] is not exist!", tenantId);
                Cookie cookie = new Cookie(CommonConstants.HTTP_HEADER.TENANT_ID, "");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                TokenUtils.sendUnauthorizedAndRedirect(response, TokenUtils.INVALID_NO_TENANT_TOKEN, "/td/" + tenantId);
                return false;
            }
        } else {
            TenantContextHolder.set("");
        }

        // TODO request中获取到则直接设置，如果没有获取到则从jwt中获取
        SessionUser user = (SessionUser) request.getSession().getAttribute(CommonConstants.User.USER_LOGIN_STATE);
        if (user != null) {
            TenantContextHolder.set(user.getTenantId());
            SessionUserHelper.setUser(user);
            return true;
        }

        RequestContext.setIpContext(RequestHelper.getClientIpAddress());

        boolean ignore = matchAnonUrls(uri);
        if (ignore) {
            return true;
        }

        String token = TokenUtils.getToken(request);
        if (StringUtils.isEmpty(token)) {
            // TODO ?
            return true;
        }

        String feignSign = request.getHeader(CommonConstants.HTTP_HEADER.FEIGN_SIGN);
        if (StringUtils.isNotEmpty(feignSign)) {
            // private static final String SECRET_KEY = "SecretKey123!";
            // 设置feign调用加密标识头设置
            String md5Code = DigestUtils.md5Hex(token + SECRET_KEY);
            if (!feignSign.equals(md5Code)) {
                logger.error("feign sign error!");
                TokenUtils.sendUnauthorized(response, TokenUtils.INVALID_FEIGN_TOKEN);
                return false;
            }
            return true;
        }

        if (!cacheService.isExist(USER_TOKEN + token)) {
            logger.info("token [{}] is not exist.", token);
            TokenUtils.sendUnauthorizedAndRedirect(response, TokenUtils.INVALID_LOGOUT_TOKEN_TOKEN, getUserLoginPage());
            return false;
        }

        SessionUser sessionUser = jwtTokenProvider.validateUserToken(token);
        if (sessionUser != null) {
            TokenHolder.set(token);
            if (StringUtils.isEmpty(TenantContextHolder.get())) {
                TenantContextHolder.set(sessionUser.getTenantId());
            }
        }
        SessionUserHelper.setUser(sessionUser);
        SessionUserHelper.setUserToken(token);
        cacheService.setExpire(USER_TOKEN + token, sessionExpireMinutes(), TimeUnit.MINUTES);

        return true;
    }

    private String getUserLoginPage() {
        String tenantId = TenantContextHolder.get();
        return StringUtils.isEmpty(tenantId) ? CommonConstants.URI.USER_LOGIN : "/td/" + tenantId;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContextHolder.clear();
        SessionUserHelper.clearUser();
        SessionUserHelper.clearUserToken();
    }

    public boolean matchAnonUrls(String url) {
        return anonUrls.stream().anyMatch(anonUrl -> antPathMatcher.match(anonUrl, url));
    }

    public void addAnonUrls(String... urls) {
        anonUrls.addAll(Arrays.stream(urls).collect(Collectors.toSet()));
    }

    public void addAnonUrls(Set<String> urls) {
        anonUrls.addAll(urls);
    }

    public List<String> getAnonUrls() {
        return anonUrls;
    }

    /**
     * 会话有效时间（单位分钟），默认60分钟
     */
    private long sessionExpireMinutes() {
        // 查询配置管理的时间，如果存在则覆盖
        long timeout = 60L;
        // LoginConfig loginConfigVO = loginConfigService.findLoginConfigVO();
        return timeout;
    }
}
