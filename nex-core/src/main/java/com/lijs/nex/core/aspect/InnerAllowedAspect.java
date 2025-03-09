package com.lijs.nex.core.aspect;

import com.lijs.nex.common.base.constant.CommonConstants;
import com.lijs.nex.common.base.exception.AccessDeniedException;
import com.lijs.nex.core.annotation.InnerAllowed;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ljs
 * @date 2025-02-07
 * @description
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class InnerAllowedAspect implements Ordered {

    private final HttpServletRequest request;

    @SneakyThrows
    @Before("@within(innerAllowed) || @annotation(innerAllowed)")
    public void around(JoinPoint point, InnerAllowed innerAllowed) {
        if (innerAllowed == null) {
            Class<?> clazz = point.getTarget().getClass();
            innerAllowed = AnnotationUtils.findAnnotation(clazz, InnerAllowed.class);
        }
        String header = request.getHeader(CommonConstants.HTTP_HEADER.REQUEST_FROM);
        if (innerAllowed != null && innerAllowed.enable() && !CommonConstants.HTTP_HEADER.REQUEST_FROM_INNER.equals(header)) {
            log.warn("接口 {} 没有访问权限", point.getSignature().getName());
            throw new AccessDeniedException("Access denied");
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

}
