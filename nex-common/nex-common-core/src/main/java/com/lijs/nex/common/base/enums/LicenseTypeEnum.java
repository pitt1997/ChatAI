package com.lijs.nex.common.base.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author ljs
 * @date 2024-11-05
 * @description
 */
public enum LicenseTypeEnum {

    /**
     * 正式授权
     */
    OFFICIAL("1"),

    /**
     * 临时授权
     */
    TEMPORARY("2");

    private String code;

    LicenseTypeEnum(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    /**
     * 校验是否属于当前枚举值
     */
    public static boolean isValidEnum(String code) {
        for (LicenseTypeEnum licenseTypeEnum : LicenseTypeEnum.values()) {
            if (licenseTypeEnum.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static LicenseTypeEnum parse(String code) {
        for (LicenseTypeEnum licenseTypeEnum : LicenseTypeEnum.values()) {
            if (licenseTypeEnum.getCode().equals(code)) {
                return licenseTypeEnum;
            }
        }
        return null;
    }

}
