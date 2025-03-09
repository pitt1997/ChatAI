package com.lijs.nex.core.annotation;

import java.lang.annotation.*;

/**
 * 未登录可访问
 *
 * @author ljs
 * @date 2025-02-07
 * @description
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnonAllowed {

}
