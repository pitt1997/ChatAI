package com.lijs.nex.common.base.enums;

/**
 * @author ljs
 * @date 2024-11-05
 * @description
 */
public enum StatusEnum {

    NORMAL(0, "正常"),
    LOCKED(1, "锁定"),
    DISABLED(2, "禁用"),

    ;

    StatusEnum(Integer code, String message) {
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
