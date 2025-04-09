package com.lijs.chatai.common.mybatis.service;

import com.lijs.chatai.common.base.tenant.Tenant;
import com.lijs.chatai.common.mybatis.config.DataSourceRouting;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @author ljs
 * @date 2024-10-06
 * @description 动态数据源接口服务
 */
@Service
public class DataSourceService {

    private final Logger logger = LoggerFactory.getLogger(DataSourceService.class);

    @Resource
    private DataSourceRouting dataSourceRouting;

    private static final String MYSQL = "MySQL";

    private static final String POSTGRESQL = "PostgreSQL";

    /**
     * 从配置文件中读取数据库类型
     */
    @Value("${platform.database-type:MySQL}")
    private String databaseType;

    /**
     * 创建租户数据源
     */
    public DataSource createTenantDataSource(Tenant tenant) {
        HikariDataSource dataSource = new HikariDataSource();
        // 根据数据库类型设置不同的 JDBC URL（默认PGSQL）
        if (MYSQL.equals(databaseType)) {
            String url = "jdbc:mysql://" +
                    tenant.getTenantSettings().getDatabaseIp() + ":" + tenant.getTenantSettings().getDatabasePort() +
                    "/" + tenant.getCode() + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setJdbcUrl(url);
        } else if (POSTGRESQL.equals(databaseType)) {
            String url = "jdbc:postgresql://" +
                    tenant.getTenantSettings().getDatabaseIp() + ":" + tenant.getTenantSettings().getDatabasePort() +
                    "/" + tenant.getCode() + "?reWriteBatchedInserts=true";
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setJdbcUrl(url);
        } else {
            logger.error("不支持的数据库类型");
            return null;
        }
        dataSource.setUsername(tenant.getTenantSettings().getDatabaseUsername());
        dataSource.setPassword(tenant.getTenantSettings().getDatabasePassword());
        // 空闲时间
        dataSource.setIdleTimeout(MINUTES.toMillis(3));
        // 最小空闲连接数
        dataSource.setMinimumIdle(0);
        // 最大连接数
        dataSource.setMaximumPoolSize(5);
        return dataSource;
    }

    /**
     * 新建租户数据源,并加入到动态数据源中
     */
    public boolean initTenantDataSource(Tenant tenant) {
        try {
            DataSource ds = this.createTenantDataSource(tenant);
            this.addTenantDataSource(ds, tenant.getCode());
        } catch (Exception e) {
            logger.error("初始化租户动态数据源异常:", e);
            return false;
        }
        return true;
    }

    /**
     * 添加新的租户数据源到动态数据源
     */
    public void addTenantDataSource(DataSource dataSource, String tenantCode) {
        if (dataSourceRouting.getResolvedDataSources().containsKey(tenantCode)) {
            return;
        }
        // 获取当前的数据源映射
        Map<Object, DataSource> resolvedDataSources = dataSourceRouting.getResolvedDataSources();
        // 创建新的数据源映射，保留现有的，添加新的租户数据源
        Map<Object, Object> saveDataSources = new HashMap<>(resolvedDataSources);
        saveDataSources.put(tenantCode, dataSource);
        dataSourceRouting.setDataSources(saveDataSources);
        dataSourceRouting.afterPropertiesSet();
    }

}
