package com.lijs.chatai.common.base.enums;

public enum ServiceEnum {

    SYSTEM("system", "系统中心服务", "platform.service.system"),
    USER("user", "用户中心服务", "platform.service.user"),
    RESOURCE("resource", "资源中心服务", "platform.service.resource"),
    AUTHENTICATION_MANAGER("authentication_manager", "认证中心服务", "platform.service.authentication_manager"),
    AUTHORIZATION("authorization", "授权中心服务", "platform.service.authorization"),
    AUDIT("audit", "审计中心服务", "platform.service.audit"),
    AUTHENTICATION("authentication", "身份认证服务", "platform.service.authentication"),
    SSO_PROTOCOLS("sso_protocols", "标准协议服务", "platform.service.sso_protocols"),
    ;

    private String code;
    private String name;
    private String propertyKey;

    ServiceEnum(String code, String name, String propertyKey) {
        this.code = code;
        this.name = name;
        this.propertyKey = propertyKey;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getPropertyKey() {
        return propertyKey;
    }
}
