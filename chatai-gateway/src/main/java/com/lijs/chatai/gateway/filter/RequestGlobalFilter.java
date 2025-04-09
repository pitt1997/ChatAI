package com.lijs.chatai.gateway.filter;

import com.lijs.chatai.common.base.constant.CommonConstants;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * 全局拦截器
 *
 * @author ljs
 * @date 2025-02-06
 * @description
 */
public class RequestGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 清除来自外部的特别标识请求头（避免伪造）
        ServerHttpRequest request = exchange.getRequest().mutate().headers(httpHeaders -> {
            // httpHeaders.remove(CommonConstants.HTTP_HEADER.FEIGN_SIGN);
            httpHeaders.remove(CommonConstants.HTTP_HEADER.REQUEST_FROM);
            // 设置请求时间
            httpHeaders.put(CommonConstants.HTTP_HEADER.REQUEST_START_TIME,
                    Collections.singletonList(String.valueOf(System.currentTimeMillis())));
        }).build();

        // 记录原始请求 URL
        addOriginalRequestUrl(exchange, request.getURI());
        // 处理路径前缀（去掉一级路径前缀） eg. 原始路径：/api/user/get 处理后路径：/user/get（去掉 api ）
        String rawPath = request.getURI().getRawPath();
        String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, "/"))
                .skip(1L) // 跳过第一个部分
                .collect(Collectors.joining("/"));

        // 构造新的请求对象
        ServerHttpRequest newRequest = request.mutate().path(newPath).build();
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());

        return chain.filter(exchange.mutate().request(newRequest.mutate().build()).build());
    }

    @Override
    public int getOrder() {
        // 数值越小，优先级越高
        // 鉴权过滤器 (AuthenticationFilter) 可能是 -1~5
        // 日志或监控相关过滤器一般 10 以上
        return 10;
    }
}
