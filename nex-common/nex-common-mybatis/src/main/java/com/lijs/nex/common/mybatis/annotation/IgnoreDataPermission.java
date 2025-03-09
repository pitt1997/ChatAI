package com.lijs.nex.common.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @author ljs
 * @date 2024-10-07
 * @description 该注解用于标识某个方法不受数据权限控制
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IgnoreDataPermission {

}
