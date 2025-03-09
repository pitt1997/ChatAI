package com.lijs.nex.common.web.response;

import com.lijs.nex.common.base.constant.CommonConstants;
import com.lijs.nex.common.base.crypto.AESCrypto;
import com.lijs.nex.common.base.crypto.RSACrypto;
import com.lijs.nex.common.base.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResponseBodyAdviceHandler implements ResponseBodyAdvice {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supports(MethodParameter methodParameter, Class clazz) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class clazz, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletRequest r = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String key = r.getHeader(CommonConstants.HTTP_HEADER.API_KEY);
        return this.encryptResponseBody(r, body, key);
    }

    public Object encryptResponseBody(HttpServletRequest request, Object body, String key) {
        if (body == null) {
            return null;
        }
        try {
            String dataEncrypt = request.getHeader(CommonConstants.HTTP_HEADER.DATA_ENCRYPT);
            if (StringUtils.isNotBlank(dataEncrypt)) {
                int encryptType = Integer.parseInt(dataEncrypt);
                if (request.getRequestURI().contains("/api")) {
                    if (encryptType == 1) {
                        return encryptBody(body, key);
                    } else if (encryptType == 2) {
                        return encryptAsymmetricBody(body, key);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("response加密异常", e);
        }
        return body;
    }

    private Object encryptBody(Object body, String key) {
        return AESCrypto.encrypt(JsonUtils.toJson(body), key, false, false);
    }

    private Object encryptAsymmetricBody(Object body, String key) {
        return RSACrypto.encryptWithStringKey(JsonUtils.toJson(body), key);
    }


}
