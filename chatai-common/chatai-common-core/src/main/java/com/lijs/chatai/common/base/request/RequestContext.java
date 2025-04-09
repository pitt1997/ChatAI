package com.lijs.chatai.common.base.request;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author ljs
 * @date 2024-11-04
 * @description 客户端ip请求上下文
 */
public class RequestContext {

    /**
     * 请求客户端IP上下文
     */
    private static final ThreadLocal<String> requestIpHolder = new TransmittableThreadLocal<>();

    public static void setIpContext(String request) {
        requestIpHolder.set(request);
    }

    public static String getIpContext() {
        return requestIpHolder.get();
    }

    public static void removeIpContext() {
        requestIpHolder.remove();
    }

}
