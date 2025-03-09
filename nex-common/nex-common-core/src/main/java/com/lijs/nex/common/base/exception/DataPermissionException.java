package com.lijs.nex.common.base.exception;

/**
 * 数据权限异常
 */
public class DataPermissionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataPermissionException() {
        super();
    }

    public DataPermissionException(String message) {
        super(message);
    }

    public DataPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataPermissionException(Throwable cause) {
        super(cause);
    }

    protected DataPermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
