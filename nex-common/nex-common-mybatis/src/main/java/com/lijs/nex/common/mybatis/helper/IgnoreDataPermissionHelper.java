package com.lijs.nex.common.mybatis.helper;

/**
 * 绕过数据权限本地线程，存储是否绕过标识
 */
public class IgnoreDataPermissionHelper {

    private static final ThreadLocal<Boolean> IGNORE_DATA_PERMISSION_CONTEXT = ThreadLocal.withInitial(() -> null);

    public static void set(Boolean ignore) {
        IGNORE_DATA_PERMISSION_CONTEXT.set(ignore);
    }

    public static Boolean get() {
        return IGNORE_DATA_PERMISSION_CONTEXT.get();
    }

    public static void remove() {
        IGNORE_DATA_PERMISSION_CONTEXT.remove();
    }

}
