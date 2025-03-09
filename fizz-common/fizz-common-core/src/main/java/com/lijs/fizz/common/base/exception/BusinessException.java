package com.lijs.fizz.common.base.exception;

import com.lijs.fizz.common.base.enums.ErrorCodeEnum;

/**
 * @author ljs
 * @date 2022-06-01
 * @description 自定义业务异常类
 */
public class BusinessException extends RuntimeException {

    private int code;

    private String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.code = errorCodeEnum.getCode();
        this.description = errorCodeEnum.getDescription();
    }

    public BusinessException(ErrorCodeEnum errorCodeEnum, String description) {
        super(errorCodeEnum.getMessage());
        this.code = errorCodeEnum.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
