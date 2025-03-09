package com.lijs.fizz.common.web.controller;

import com.lijs.fizz.common.base.constant.CommonConstants;
import com.lijs.fizz.common.base.session.SessionUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(CommonConstants.User.USER_LOGIN_STATE);
        SessionUser user = (SessionUser) userObj;
        // TODO 是否管理员
        // return user != null && user.getUserRole() == CommonConstants.User.ADMIN_ROLE;
        return true;
    }

    public HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("当前线程无法获取 HttpServletRequest");
        }
        return attributes.getRequest();
    }

}
