package com.lijs.fizz.common.base.session;

import com.alibaba.ttl.TransmittableThreadLocal;

public class SessionUserHolder {

    private static final ThreadLocal<SessionUser> contextUserHolder = new TransmittableThreadLocal<>();

    public static void set(SessionUser sessionUser) {
        contextUserHolder.set(sessionUser);
    }

    public static SessionUser get() {
        return contextUserHolder.get();
    }

    public static void remove() {
        contextUserHolder.remove();
    }

}
