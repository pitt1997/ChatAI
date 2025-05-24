package com.lijs.chatai.common.web.request;

import com.lijs.chatai.common.base.exception.SystemException;
import com.lijs.chatai.common.base.request.RequestHelper;
import com.lijs.chatai.common.base.session.SessionUser;
import com.lijs.chatai.common.base.session.SessionUserHelper;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;

@RestControllerAdvice
public class RequestBodyAdviceHandler implements RequestBodyAdvice {

    private static final ThreadLocal<RequestInfo> REQUEST_INFO_CONTEXT = ThreadLocal.withInitial(() -> null);

    public RequestInfo getCurrentRequest() {
        RequestInfo requestInfo = REQUEST_INFO_CONTEXT.get();
        if (requestInfo == null) {
            HttpServletRequest request = RequestHelper.getCurrentRequest();
            if (request == null) {
                throw new SystemException("Unable to retrieve request");
            }
            requestInfo = generateBaseRequestInfo(RequestHelper.getCurrentRequest());
        }

        SessionUser currentSessionUser = SessionUserHelper.getUser();
        if (currentSessionUser != null) {
            requestInfo.setUser(currentSessionUser.getUserId());
        }
        return requestInfo;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        HttpServletRequest request = RequestHelper.getCurrentRequest();
        if (request == null) {
            throw new SystemException("Unable to retrieve request");
        }
        String method = request.getMethod();
        if (HttpMethod.POST.name().equalsIgnoreCase(method) || HttpMethod.PUT.name().equalsIgnoreCase(method)) {
            RequestInfo requestInfo = REQUEST_INFO_CONTEXT.get();
            if (requestInfo == null) {
                requestInfo = generateBaseRequestInfo(request);
                REQUEST_INFO_CONTEXT.set(requestInfo);
            }
            return true;
        }
        return false;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        RequestInfo requestInfo = REQUEST_INFO_CONTEXT.get();
        requestInfo.setBody(body.toString().length() > 10000 ? body.toString().substring(0, 9999) : body.toString());
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    /**
     * 不包含 body 信息
     */
    private RequestInfo generateBaseRequestInfo(HttpServletRequest request) {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setIp(RequestHelper.getClientIpAddress());
        requestInfo.setMethod(request.getMethod());
        requestInfo.setUrl(request.getRequestURI());
        SessionUser sessionUserInfo = SessionUserHelper.getUser();
        if (sessionUserInfo != null) {
            requestInfo.setUser(sessionUserInfo.getUserId());
        }
        requestInfo.setReferrer(request.getRemoteHost());
        requestInfo.setQueryString(request.getQueryString());
        return requestInfo;
    }

    public static class RequestInfo {

        private String method;
        private String body;
        private String queryString;
        private String ip;
        private String user;
        private String referrer;
        private String url;
        private String errorMsg;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getQueryString() {
            return queryString;
        }

        public void setQueryString(String queryString) {
            this.queryString = queryString;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getReferrer() {
            return referrer;
        }

        public void setReferrer(String referrer) {
            this.referrer = referrer;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        @Override
        public String toString() {
            return "RequestInfo{" +
                    "method='" + method + '\'' +
                    ", body='" + body + '\'' +
                    ", queryString='" + queryString + '\'' +
                    ", ip='" + ip + '\'' +
                    ", user='" + user + '\'' +
                    ", referrer='" + referrer + '\'' +
                    ", url='" + url + '\'' +
                    ", errorMsg='" + errorMsg + '\'' +
                    '}';
        }
    }
}
