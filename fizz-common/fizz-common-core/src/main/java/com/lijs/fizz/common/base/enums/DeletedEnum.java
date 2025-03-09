package com.lijs.fizz.common.base.enums;

/**
 * @author ljs
 * @date 2024-11-05
 * @description
 */
public enum DeletedEnum {

    NOT_DELETED(0, "未删除"),
    DELETED(1, "已删除");

    DeletedEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
