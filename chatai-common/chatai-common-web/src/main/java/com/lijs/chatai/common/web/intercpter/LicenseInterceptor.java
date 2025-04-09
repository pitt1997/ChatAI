package com.lijs.chatai.common.web.intercpter;

import com.lijs.chatai.common.web.license.LicenseCacheService;
import com.lijs.chatai.common.base.constant.CommonConstants;
import com.lijs.chatai.common.base.license.LicenseInfo;
import com.lijs.chatai.common.base.license.LicenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class LicenseInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(LicenseInterceptor.class);

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private LicenseCacheService licenseCacheService;

    private final List<String> anonUrls = new CopyOnWriteArrayList<>();

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public LicenseInterceptor(String... anonUrls) {
        if (anonUrls != null) {
            this.anonUrls.addAll(Arrays.stream(anonUrls).collect(Collectors.toSet()));
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        if (matchAnon(uri)) {
            return true;
        }

        // 从cache中获取license
        LicenseInfo licenseInfo = licenseCacheService.getLicense();
        if (!licenseService.isUsable(licenseInfo)) {
            logger.error("current [{}] license is available.", uri);
            // 重定向到license导入页面
            response.addHeader(CommonConstants.HTTP_HEADER.REDIRECT, CommonConstants.URI.LICENSE_IMPORT);
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    public boolean matchAnon(String uri) {
        // TODO 调试暂时放行所有URL请求
        // return anonUrls.stream().anyMatch(anonUrl -> antPathMatcher.match(anonUrl, uri));
        return true;
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
}
