package com.lijs.chatai.common.web.config;

import com.lijs.chatai.common.base.exception.GlobalExceptionHandler;
import com.lijs.chatai.common.base.token.JwtTokenProvider;
import com.lijs.chatai.common.web.request.RequestBodyAdviceHandler;
import com.lijs.chatai.common.mybatis.config.DataSourceRouting;
import com.lijs.chatai.common.web.intercpter.LicenseInterceptor;
import com.lijs.chatai.common.web.intercpter.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.Resource;

/**
 * @author ljs
 * @date 2024-10-31
 * @description
 */
@Configuration
@ConditionalOnClass(WebMvcConfigurer.class)
@EnableConfigurationProperties(ApiPermissionConfig.class)
@Order(1)
public class WebMvcConfig implements WebMvcConfigurer {

    // 注意这里需要通过类型来引入，因为有地方通过方法名称的方式注入该bean但是不是这个名字
    // @Resource
    @Lazy
    @Autowired
    private DataSourceRouting dataSourceRouting;

    @Bean
    @ConditionalOnMissingBean
    public ApiPermissionConfig apiPermissionConfig() {
        return new ApiPermissionConfig();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "jwtTokenProvider")
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    @ConditionalOnMissingBean(name = "securityInterceptor")
    public SecurityInterceptor securityInterceptor() {
        SecurityInterceptor securityInterceptor = new SecurityInterceptor(dataSourceRouting);
        securityInterceptor.addAnonUrls(apiPermissionConfig().getAnonUrls());
        return securityInterceptor;
    }

    @Bean
    @ConditionalOnMissingBean(name = "licenseInterceptor")
    public LicenseInterceptor licenseInterceptor() {
        LicenseInterceptor licenseInterceptor = new LicenseInterceptor();
        licenseInterceptor.addAnonUrls(apiPermissionConfig().getAnonUrls());
        return licenseInterceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestBodyAdviceHandler postPutInfoAdvice() {
        return new RequestBodyAdviceHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(licenseInterceptor());
        registry.addInterceptor(securityInterceptor());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8000") // 确保配置前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);  // 确保支持携带 Cookie
    }

}
