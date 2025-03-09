package com.lijs.fizz.common.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "platform.api-permission")
public class ApiPermissionConfig {

    /**
     * 可匿名访问（不登录可访问）URL
     */
    private Set<String> anons = new HashSet<>();

    public Set<String> getAnonUrls() {
        return anons;
    }

    public void setAnonUrls(Set<String> anonUrls) {
        this.anons = anonUrls;
    }

}
