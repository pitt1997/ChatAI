package com.lijs.nex.common.base.utils;

import com.lijs.nex.common.base.enums.ErrorCodeEnum;
import com.lijs.nex.common.base.response.BaseResponse;

/**
 * @author ljs
 * @date 2022-06-01
 * @description
 */
public class ResultUtils {

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "success");
    }

    public static <T> BaseResponse<T> error(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }

    public static <T> BaseResponse<T> error(ErrorCodeEnum errorCodeEnum) {
        return new BaseResponse<>(errorCodeEnum);
    }

    public static <T> BaseResponse<T> error(ErrorCodeEnum errorCodeEnum, String message, String description) {
        return new BaseResponse<>(errorCodeEnum.getCode(), null, message, description);
    }

    public static BaseResponse<String> error(ErrorCodeEnum errorCodeEnum, String description) {
        return new BaseResponse<>(errorCodeEnum.getCode(), errorCodeEnum.getMessage(), description);
    }

}
