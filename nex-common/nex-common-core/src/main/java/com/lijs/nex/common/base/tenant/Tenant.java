package com.lijs.nex.common.base.tenant;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author ljs
 * @date 2024-10-24
 * @description 多租户对象
 *
 * 对于多租户架构，数据隔离方式一般有以下几种：
 * 共享数据库，独立 Schema：每个租户使用同一个数据库，但使用不同的 Schema 进行数据隔离。
 * 共享数据库，共享 Schema：所有租户共享同一个数据库和 Schema，通过 tenant_id 字段进行数据隔离。
 * 独立数据库：每个租户使用完全独立的数据库进行隔离。
 *
 */
public class Tenant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 租户编码
     */
    private String code;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 租户域名
     */
    private String domain;

    /**
     * 租户配置对象
     * 1、数据库配置信息
     * 2、功能权限点、数据权限点
     * 3、license授权用户数量
     * ...
     */
    private TenantSettings tenantSettings;

    /**
     * 状态   启用/禁用/删除 NORMAL/DISABLE/DEL
     */
    private String status;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp expireTime;

    public Tenant() {
    }

    public Tenant(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
    }

    public TenantSettings getTenantSettings() {
        return tenantSettings;
    }

    public void setTenantSettings(TenantSettings tenantSettings) {
        this.tenantSettings = tenantSettings;
    }
}
