package com.lijs.chatai.core.annotation;

import java.lang.annotation.*;

/**
 * 内部服务之间可访问，默认都可以访问，如果需要单独控制服务之间还需要鉴权则设置为false （@InnerAllowed(value = false)）
 *
 * @author ljs
 * @date 2025-02-07
 * @description
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InnerAllowed {

    /**
     * 是否 AOP 统一处理
     * 默认 true，表示 AOP 统一拦截处理
     */
    boolean enable() default true;

    /**
     * 预留功能：指定某些字段是否需要特殊校验
     */
    String[] requiredFields() default {};

}
