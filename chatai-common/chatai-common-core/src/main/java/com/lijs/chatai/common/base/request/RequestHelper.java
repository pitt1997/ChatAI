package com.lijs.chatai.common.base.request;

import com.lijs.chatai.common.base.constant.CommonConstants;
import com.lijs.chatai.common.base.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 封装常见的Request方法
 */
public class RequestHelper {

    private static final Logger logger = LoggerFactory.getLogger(RequestHelper.class);
    private static final String LOCAL_IP = "127.0.0.1";
    private static final String UNKNOWN = "unknown";

    /**
     * 判断是否为ajax请求
     */
    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    /**
     * 获取 request
     */
    public static HttpServletRequest getCurrentRequest() {
        return (HttpServletRequest) Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(attributes -> ((ServletRequestAttributes) attributes).getRequest())
                .orElse(null);
    }

    /**
     * 获取客户端 ip 地址
     */
    public static String getClientIpAddress() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return LOCAL_IP;
        }
        return getClientIpAddress(request);
    }

    /**
     * request中获取客户端请求的IP
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        // feign请求设置本机ip
        String feignRequest = request.getHeader(CommonConstants.HTTP_HEADER.FEIGN_SIGN);
        if (feignRequest != null) {
            return LOCAL_IP;
        }
        // 请求头中获取
        String ip = request.getHeader("REMOTE-HOST");
        if (isUnknownIp(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (isUnknownIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isUnknownIp(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (isUnknownIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isUnknownIp(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (isUnknownIp(ip)) {
            ip = request.getRemoteAddr();
        }
        // 经过网关代理后，会出现获取到多个源IP的情况，例如“172.168.1.10,192.168.1.20”
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? LOCAL_IP : ip;
    }

    private static boolean isUnknownIp(String ip) {
        return ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip);
    }

    /**
     * 将所有的请求参数转换字符串
     * 例如: https://chatai.com/login?param1=a&param2=b,c
     */
    public static String requestParamsToString(ServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String[]> map = request.getParameterMap();
        if (map == null || map.size() == 0) {
            return "";
        }
        String arrayJoiner = ",";
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String joiner = "&";
            String key = entry.getKey();
            String[] value = entry.getValue();
            if (value != null && value.length == 1) {
                stringBuilder.append(joiner).append(key).append("=").append(value[0]);
            } else {
                stringBuilder.append(joiner).append(key).append("=").append(StringUtils.join(value, arrayJoiner));
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 将 HttpServletRequest 的请求参数转换为 Map<String, Object>
     * 如果参数有多个值，则以逗号连接。
     */
    public static Map<String, Object> getRequestParams(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> resultMap = new HashMap<>();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();

            if (values == null) {
                resultMap.put(key, "");
            } else if (values.length == 1) {
                resultMap.put(key, values[0]);
            } else {
                // 多值参数用逗号连接
                String joinedValues = String.join(",", values);
                resultMap.put(key, joinedValues);
            }
        }
        return resultMap;
    }

    /**
     * 获取整型参数
     */
    public static Integer getIntegerParam(HttpServletRequest request, String paramName) {
        try {
            return Integer.valueOf(request.getParameter(paramName));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 获取客户端请求信息
     */
    public static ClientContext clientInfo(HttpServletRequest request) {
        ClientContext info = new ClientContext();
        info.setRequestTime(System.currentTimeMillis());
        info.setClientIp(getClientIpAddress(request));
        info.setReferer(request.getHeader("referer"));
        info.setUri(request.getRequestURI());
        String ua = request.getHeader("User-Agent");
        info.setUserAgents(ua);
        info.setPlatformOS(getPlatformFromUserAgent(ua));
        return info;
    }

    /**
     * 根据 User-Agent 获取平台信息
     */
    private static String getPlatformFromUserAgent(String userAgent) {
        if (userAgent == null) {
            return "Unknown";
        }
        userAgent = userAgent.toUpperCase();

        Map<String, String> platformMap = new HashMap<>();
        platformMap.put("WINDOWS NT 5.1", "WindowsVista");
        platformMap.put("WINDOWS NT 6.1", "Windows7");
        platformMap.put("WINDOWS NT 6.2", "Windows8");
        platformMap.put("WINDOWS NT 10", "Windows10");
        platformMap.put("MAC", "MacOS");
        platformMap.put("LINUX", "Linux");
        platformMap.put("ANDROID", "Android");
        platformMap.put("IPHONE", "iOS");
        platformMap.put("IOS", "iOS");

        for (Map.Entry<String, String> entry : platformMap.entrySet()) {
            if (userAgent.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "Unknown"; // 未匹配到时返回 "Unknown"
    }

    public static class ClientContext {

        /**
         * 请求发送时间
         */
        private Long requestTime;

        /**
         * 请求端 IP 地址
         */
        private String clientIp;

        /**
         * 客户端操作系统
         */
        private String platformOS;

        /**
         * 客户端指纹信息
         */
        private String userAgents;

        /**
         * HTTP 引用信息
         */
        private String referer;

        /**
         * 请求端点 URI
         */
        private String uri;

        /**
         * 请求参数
         */
        private Map<String, Object> params;

        public Long getRequestTime() {
            return requestTime;
        }

        public void setRequestTime(Long requestTime) {
            this.requestTime = requestTime;
        }

        public String getClientIp() {
            return clientIp;
        }

        public void setClientIp(String clientIp) {
            this.clientIp = clientIp;
        }

        public String getPlatformOS() {
            return platformOS;
        }

        public void setPlatformOS(String platformOS) {
            this.platformOS = platformOS;
        }

        public String getUserAgents() {
            return userAgents;
        }

        public void setUserAgents(String userAgents) {
            this.userAgents = userAgents;
        }

        public String getReferer() {
            return referer;
        }

        public void setReferer(String referer) {
            this.referer = referer;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public Map<String, Object> getParams() {
            return params;
        }

        public void setParams(Map<String, Object> params) {
            this.params = params;
        }

    }
}