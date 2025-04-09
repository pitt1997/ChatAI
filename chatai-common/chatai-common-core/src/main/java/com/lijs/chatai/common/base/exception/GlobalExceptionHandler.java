package com.lijs.chatai.common.base.exception;

import com.lijs.chatai.common.base.enums.ErrorCodeEnum;
import com.lijs.chatai.common.base.response.BaseResponse;
import com.lijs.chatai.common.base.utils.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Spring 提供的一个注解 @RestControllerAdvice，用于集中处理 @RestController 控制器中抛出的异常。
 * 它结合了 @ControllerAdvice 和 @ResponseBody 的功能，使得异常处理更简洁高效，常用于构建全局异常处理机制。
 * 利用 Spring AOP 切面在调用方法前后进行额外处理
 *
 * @author ljs
 * @date 2022-06-03
 * @description 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 捕获 {@link BusinessException} 异常
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        logger.error("businessException -> " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());

    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        logger.error("runtimeException -> ", e);
        return ResultUtils.error(ErrorCodeEnum.PARAMS_ERROR, e.getMessage(), "");
    }

}
