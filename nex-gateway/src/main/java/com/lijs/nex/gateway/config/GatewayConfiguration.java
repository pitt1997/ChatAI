package com.lijs.nex.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijs.nex.gateway.filter.RequestGlobalFilter;
import com.lijs.nex.gateway.handler.GatewayGlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ljs
 * @date 2025-02-06
 * @description
 */
@Configuration(proxyBeanMethods = false)
public class GatewayConfiguration {

    /**
     * 创建PigRequest全局过滤器
     * @return PigRequest全局过滤器
     */
    @Bean
    public RequestGlobalFilter requestGlobalFilter() {
        return new RequestGlobalFilter();
    }

    /**
     * 创建全局异常处理程序
     * @param objectMapper 对象映射器
     * @return 全局异常处理程序
     */
    @Bean
    public GatewayGlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
        return new GatewayGlobalExceptionHandler(objectMapper);
    }

}
