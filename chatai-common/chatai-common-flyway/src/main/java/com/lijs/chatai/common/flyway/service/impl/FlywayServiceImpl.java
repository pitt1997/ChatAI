package com.lijs.chatai.common.flyway.service.impl;

import com.lijs.chatai.common.base.tenant.Tenant;
import com.lijs.chatai.common.base.tenant.TenantSettings;
import com.lijs.chatai.common.flyway.service.FlywayService;
import com.lijs.chatai.common.mybatis.config.DataSourceRouting;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import jakarta.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@ConditionalOnClass(Flyway.class)
public class FlywayServiceImpl implements FlywayService {

    private final Logger logger = LoggerFactory.getLogger(FlywayServiceImpl.class);

    public static final String FLYWAY_LOCATIONS = "classpath:db/migration/mysql/";

    public static final String FLYWAY_BASELINE_VERSION = "0";

    /**
     * 在执行迁移时自动对未被 Flyway 管理的数据库进行基线处理
     */
    public static final boolean FLYWAY_BASELINE_ON_MIGRATE = true;

    @Lazy
    @Autowired(required = false)
    private Flyway flyway;

//    @Resource
//    private DataSourceRouting dataSourceRouting;

    /**
     * 更新所有数据源
     */
    @Override
    public void migrate() {
//        Map<Object, DataSource> dataSourceMap = new HashMap<>(dataSourceRouting.getResolvedDataSources());
//        dataSourceMap.forEach((key, ds) -> {
//            try {
//                this.migrateDataSource(ds);
//            } catch (Exception e) {
//                logger.error(e.getMessage(), e);
//            }
//        });
    }

    @Override
    public void migrateDataSource(DataSource ds) {
        Flyway f = Flyway.configure().
                dataSource(ds). // 设置数据源
                        locations(getLocations()) // 设置迁移脚本位置，classpath:db/migration/mysql/
                .baselineVersion(FLYWAY_BASELINE_VERSION) // 基线版本，默认0
                .baselineOnMigrate(FLYWAY_BASELINE_ON_MIGRATE) // 是否基线迁移，true
                .table(flyway.getConfiguration().getTable()) // 设置迁移历史表名，flyway_schema_history
                .outOfOrder(flyway.getConfiguration().isOutOfOrder()) // 是否允许乱序迁移，false
                .validateOnMigrate(flyway.getConfiguration().isValidateOnMigrate()) // 迁移脚本验证，true
                .load();
        f.migrate();
    }

    /**
     * 升级脚本存储目录
     */
    private static String[] getLocations() {
        return FLYWAY_LOCATIONS.split(",");
    }

    /**
     * 指定租户库执行flyway脚本
     */
    private void migrateDataSource(Flyway flyway, Tenant tenant) {
        org.flywaydb.core.api.configuration.Configuration configuration = flyway.getConfiguration();
        HikariDataSource value = (HikariDataSource) configuration.getDataSource();
        try (HikariDataSource dataSource = getDataSource(tenant, value)) {
            Flyway f = Flyway.configure().
                    dataSource(dataSource).
                    locations(getLocations()) // "classpath:db/migration"
                    .baselineOnMigrate(flyway.getConfiguration().isBaselineOnMigrate())//false
                    .table(flyway.getConfiguration().getTable()) //flyway_schema_history
                    .outOfOrder(flyway.getConfiguration().isOutOfOrder())//false
                    .validateOnMigrate(flyway.getConfiguration().isValidateOnMigrate())//false
                    .load();
            f.migrate();

        } catch (Exception e) {
            logger.error("flyway指定租户数据源", e);
        }
    }

    /**
     * 获取租户库的数据源
     */
    private static HikariDataSource getDataSource(Tenant tenant, HikariDataSource value) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(value.getDriverClassName());
        if (tenant.getTenantSettings() == null) {
            String tenantUrl = getTenantUrl(value.getJdbcUrl(), tenant.getCode());
            dataSource.setJdbcUrl(tenantUrl);
            dataSource.setUsername(value.getUsername());
            dataSource.setPassword(value.getPassword());
        } else {
            // 替换成租户数据源信息
            String before = StringUtils.substringBefore(value.getJdbcUrl(), "?");
            String table = StringUtils.substringAfterLast(before, "/");
            String dbName = table + "_" + tenant.getCode();
            TenantSettings settings = tenant.getTenantSettings();
            dataSource.setJdbcUrl("jdbc:postgresql://" + settings.getDatabaseIp() + ":" + settings.getDatabasePort() + "/" + dbName + "?reWriteBatchedInserts=true");
            dataSource.setUsername(settings.getDatabaseUsername());
            dataSource.setPassword(settings.getDatabasePassword());
        }
        return dataSource;
    }

    /**
     * 获取租户数据源url
     */
    public static String getTenantUrl(String url, String code) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        String before = StringUtils.substringBefore(url, "?");
        String param = StringUtils.substringAfter(url, "?");
        String urlNew = before + "_" + code;
        return StringUtils.isEmpty(param) ? urlNew : urlNew + "?" + param;
    }

}
