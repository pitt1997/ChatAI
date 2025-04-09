package com.lijs.chatai.common.base.license;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ljs
 * @date 2024-11-05
 * @description
 */
public class LicenseInfo implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 用户数量限制
     */
    private Long userLimit;

    /**
     * 用户数量限制
     */
    private Long resourceLimit;

    /**
     * 租户数量限制
     */
    private Integer tenantLimit;

    /**
     * 授权类型
     */
    private String type;

    /**
     * 授权模块（服务）
     */
    private List<String> services;

    /**
     * 临时授权结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(Long userLimit) {
        this.userLimit = userLimit;
    }

    public Long getResourceLimit() {
        return resourceLimit;
    }

    public void setResourceLimit(Long resourceLimit) {
        this.resourceLimit = resourceLimit;
    }

    public Integer getTenantLimit() {
        return tenantLimit;
    }

    public void setTenantLimit(Integer tenantLimit) {
        this.tenantLimit = tenantLimit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
