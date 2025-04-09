package com.lijs.chatai.common.feign.config;

import com.lijs.chatai.common.base.config.JacksonConfig;
import com.lijs.chatai.common.feign.interceptor.FeignSecurityInterceptor;
import com.lijs.chatai.common.base.token.JwtTokenProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter({
        JacksonConfig.class
})
public class FeignAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public FeignSecurityInterceptor feignRequestHeaderInterceptor(JwtTokenProvider jwtTokenProvider) {
        return new FeignSecurityInterceptor(jwtTokenProvider);
    }

}
