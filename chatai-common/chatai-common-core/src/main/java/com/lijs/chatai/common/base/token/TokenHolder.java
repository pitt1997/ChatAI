package com.lijs.chatai.common.base.token;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author ljs
 * @date 2024-10-07
 * @description
 */
public class TokenHolder {

    private static final ThreadLocal<String> holder = new TransmittableThreadLocal<>();

    public static String get() {
        return holder.get();
    }

    public static void set(String token) {
        holder.set(token);
    }

    public static void clear() {
        holder.remove();
    }

}
