package com.lijs.nex.common.base.tenant;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 租户Tenant信息上下文
 */
public class TenantContextHolder {

    private final static Logger logger = LoggerFactory.getLogger(TenantContextHolder.class);

    /**
     * 租户信息
     */
    private static final ThreadLocal<String> contextHolder = new TransmittableThreadLocal<>();

    public static void set(String tenantId) {
        contextHolder.set(tenantId);
    }

    public static String get() {
        String td = contextHolder.get();
        return (td == null ? "" : td);
    }

    public static void clear() {
        contextHolder.remove();
    }

    /**
     * 本地线程变量维护是否开启租户隔离变量
     */
    private static final ThreadLocal<Boolean> tenantIgnore = new TransmittableThreadLocal<>();

    public static void setIgnore(Boolean ignore) {
        tenantIgnore.set(ignore);
    }

    /**
     * 默认不忽略租户
     */
    public static Boolean getIgnore() {
        return tenantIgnore.get() != null && tenantIgnore.get();
    }

    public static void clearIgnore() {
        tenantIgnore.remove();
    }

}
