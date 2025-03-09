package com.lijs.nex.common.base.tenant;

/**
 * @author author
 * @date 2024-10-24
 * @description
 */
public class TenantSettings {

    /**
     * 租户数据库连接信息-ip
     */
    private String databaseIp;

    /**
     * 租户数据库连接信息-端口
     */
    private String databasePort;

    /**
     * 租户数据库连接信息-用户名
     */
    private String databaseUsername;

    /**
     * 租户数据库连接信息-密码
     */
    private String databasePassword;

    /**
     * 每个租户的最大资源数限制
     */
    private Long resourceLimit;

    /**
     * 每个租户的最大用户数限制
     */
    private Long userLimit;

    /**
     * license授权菜单模块
     */
    private String menus;

    public String getDatabaseIp() {
        return databaseIp;
    }

    public void setDatabaseIp(String databaseIp) {
        this.databaseIp = databaseIp;
    }

    public String getDatabasePort() {
        return databasePort;
    }

    public void setDatabasePort(String databasePort) {
        this.databasePort = databasePort;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public Long getResourceLimit() {
        return resourceLimit;
    }

    public void setResourceLimit(Long resourceLimit) {
        this.resourceLimit = resourceLimit;
    }

    public Long getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(Long userLimit) {
        this.userLimit = userLimit;
    }

    public String getMenus() {
        return menus;
    }

    public void setMenus(String menus) {
        this.menus = menus;
    }
}