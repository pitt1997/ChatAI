package com.lijs.nex.common.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.lijs.nex.common.mybatis.handler.CustomTenantHandler;
import com.lijs.nex.common.mybatis.handler.EntityAutoFillHandler;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * Mybatis-Plus 配置
 * 注意使用多数据源时，请在启动类中排除 Spring Boot 的自动装配数据源：
 * SpringBootApplication(exclude = {MybatisPlusAutoConfiguration.class, DynamicDataSourceAutoConfiguration.class})
 */
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(MybatisPlusProperties.class)
//@ConditionalOnProperty(prefix = "mybatis-plus", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MybatisPlusConfig {

    private final Logger logger = LoggerFactory.getLogger(MybatisPlusConfig.class);

    @Resource
    private DataSourceConfig dataSourceConfig;
    @Resource
    private MybatisPlusProperties mybatisPlusProperties;
    @Resource
    private ApplicationContext applicationContext;

    private static final String MYSQL = "MySQL";

    /**
     * 从配置文件中读取数据库类型
     */
    @Value("${platform.database-type:MySQL}")
    private String databaseType;

    /**
     * 主数据源
     */
    private DataSource primaryDataSource = null;

    private DataSourceRouting dataSourceRouting = null;

    /**
     * 主数据源的配置
     */
    @Primary
    @Lazy
    @Bean("primaryDataSource")
    public DataSource initPrimaryDatasource() {
        logger.info("primaryDataSource init...");
        if (primaryDataSource == null && dataSourceConfig != null && org.apache.commons.lang3.StringUtils.isNotEmpty(dataSourceConfig.getUrl())) {
            HikariDataSource hikariDataSource = new HikariDataSource();
            hikariDataSource.setDriverClassName(dataSourceConfig.getDriverClassName());
            hikariDataSource.setJdbcUrl(dataSourceConfig.getUrl());
            hikariDataSource.setUsername(dataSourceConfig.getUsername());
            hikariDataSource.setPassword(dataSourceConfig.getPassword());
            hikariDataSource.setIdleTimeout(dataSourceConfig.getHikari().getIdleTimeout() != null ? dataSourceConfig.getHikari().getIdleTimeout() : MINUTES.toMillis(5));
            hikariDataSource.setMinimumIdle(dataSourceConfig.getHikari().getMinimumIdle() != null ? dataSourceConfig.getHikari().getMinimumIdle() : 1);
            hikariDataSource.setMaximumPoolSize(dataSourceConfig.getHikari().getMaximumPoolSize() != null ? dataSourceConfig.getHikari().getMaximumPoolSize() : 8);
            primaryDataSource = hikariDataSource;
            // 检查状态
            checkPrimaryConnection(primaryDataSource);
        }
        return primaryDataSource;
    }

    /**
     * 多数据源的配置
     */
    @Lazy
    @Bean("dynamicDataSource")
    public DataSourceRouting dynamicDataSource() {
        if (dataSourceRouting == null) {
            logger.info("dynamicDataSource init...");
            dataSourceRouting = new DataSourceRouting(false);
            DataSource primaryDs = initPrimaryDatasource();
            dataSourceRouting.setDefaultDataSource(primaryDs);
            Map<Object, Object> dataSourceMap = new HashMap<>();
            dataSourceMap.put(DataSourceRouting.PRIMARY_DB, primaryDs);
            dataSourceRouting.setDataSources(dataSourceMap);
        }
        return dataSourceRouting;
    }

    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor() {
        return new TenantLineInnerInterceptor(new CustomTenantHandler());
    }

    private void checkPrimaryConnection(DataSource dataSource) {
        for (int attempt = 0; attempt < 3; attempt++) {
            try (Connection connection = dataSource.getConnection()) {
                return; // 连接成功，退出
            } catch (SQLException e) {
                logger.error("数据源初始化失败，等待重试", e);
                try {
                    Thread.sleep(getRetryDelay(attempt));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break; // 中断，退出
                }
            }
        }
        throw new RuntimeException("无法初始化主数据源，超过重试次数");
    }

    private long getRetryDelay(int attempt) {
        return (attempt < 2) ? 30000 : 60000; // 前两次30秒，最后一次60秒
    }

    /**
     * 检查 MySQL 数据库配置是否正确，返回验证结果
     *
     * @param ip     数据库 IP 地址
     * @param port   数据库端口
     * @param schema 数据库名
     * @param user   数据库用户名
     * @param passwd 数据库密码
     * @return true 如果验证成功，否则 false
     */
    private boolean checkDbConfig(String ip, Integer port, String schema, String user, String passwd) {
        Connection connection = null;
        Statement statement = null;
        try {
            // 加载 MySQL 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 构造 MySQL 连接 URL
            String url = "jdbc:mysql://" + ip + ":" + port + "/" + schema +
                    "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            // 获取数据库连接
            connection = DriverManager.getConnection(url, user, passwd);

            // 执行简单查询以验证连接是否成功
            statement = connection.createStatement();
            statement.executeQuery("SELECT 1");

            return true;
        } catch (Exception e) {
            logger.error("验证数据库配置失败", e);
            return false;
        } finally {
            // 关闭资源
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                logger.warn("关闭 Statement 时发生异常", e);
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                logger.warn("关闭 Connection 时发生异常", e);
            }
        }
    }

    /**
     * mybatis配置
     */
    @Primary
    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(ObjectProvider<Interceptor[]> interceptorsProvider,
                                                              ObjectProvider<DatabaseIdProvider> databaseIdProvider,
                                                              ObjectProvider<TypeHandler[]> typeHandlersProvider) {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        // 配置mybatis-plus、mybatis插件生效
        Interceptor[] interceptors = interceptorsProvider.getIfAvailable();
        sessionFactory.setPlugins(interceptors);
        // 配置数据源（支持数据源切换）
        sessionFactory.setDataSource(dynamicDataSource());
        // 配置扫描映射文件
        if (databaseIdProvider.getIfAvailable() != null) {
            sessionFactory.setDatabaseIdProvider(databaseIdProvider.getIfAvailable());
        }
        if (StringUtils.hasLength(this.mybatisPlusProperties.getTypeAliasesPackage())) {
            sessionFactory.setTypeAliasesPackage(this.mybatisPlusProperties.getTypeAliasesPackage());
        }
        if (this.mybatisPlusProperties.getTypeAliasesSuperType() != null) {
            sessionFactory.setTypeAliasesSuperType(this.mybatisPlusProperties.getTypeAliasesSuperType());
        }
        if (StringUtils.hasLength(this.mybatisPlusProperties.getTypeHandlersPackage())) {
            sessionFactory.setTypeHandlersPackage(this.mybatisPlusProperties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(typeHandlersProvider.getIfAvailable())) {
            sessionFactory.setTypeHandlers(typeHandlersProvider.getIfAvailable());
        }
        org.springframework.core.io.Resource[] mapperLocations = this.mybatisPlusProperties.resolveMapperLocations();
        if (!ObjectUtils.isEmpty(mapperLocations)) {
            sessionFactory.setMapperLocations(mapperLocations);
        }
        MybatisConfiguration configuration = this.mybatisPlusProperties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(this.mybatisPlusProperties.getConfigLocation())) {
            configuration = new MybatisConfiguration();
        }
        sessionFactory.setConfiguration(configuration);

        // mybatis-plus全局配置
        setGlobalConfig(sessionFactory);

        return sessionFactory;
    }

    /**
     * 参考源码MybatisPlusAutoConfiguration#setGlobalConfig
     */
    private void setGlobalConfig(MybatisSqlSessionFactoryBean sessionFactory) {
        GlobalConfig globalConfig = mybatisPlusProperties.getGlobalConfig();
        // 自动填充（MetaObjectHandler）
        if (applicationContext.getBeanNamesForType(MetaObjectHandler.class, false, false).length > 0) {
            MetaObjectHandler bean = applicationContext.getBean(MetaObjectHandler.class);
            globalConfig.setMetaObjectHandler(bean);
        }
        // 主键生成器（IKeyGenerator）
        if (applicationContext.getBeanNamesForType(IKeyGenerator.class, false, false).length > 0) {
            Map<String, IKeyGenerator> beansOfType = applicationContext.getBeansOfType(IKeyGenerator.class);
            List<IKeyGenerator> clazzList = new ArrayList<>();
            beansOfType.forEach((k, v) -> clazzList.add(v));
            globalConfig.getDbConfig().setKeyGenerators(clazzList);
        }
        // sql注入器（ISqlInjector）
        if (applicationContext.getBeanNamesForType(ISqlInjector.class, false, false).length > 0) {
            ISqlInjector bean = applicationContext.getBean(ISqlInjector.class);
            globalConfig.setSqlInjector(bean);
        }
        // ID生成器（IdentifierGenerator）
        if (applicationContext.getBeanNamesForType(IdentifierGenerator.class, false, false).length > 0) {
            IdentifierGenerator bean = applicationContext.getBean(IdentifierGenerator.class);
            globalConfig.setIdentifierGenerator(bean);
        }
        sessionFactory.setGlobalConfig(globalConfig);
    }

    private void setConfiguration(MybatisSqlSessionFactoryBean sessionFactory) {
        MybatisConfiguration configuration = this.mybatisPlusProperties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(this.mybatisPlusProperties.getConfigLocation())) {
            configuration = new MybatisConfiguration();
        }
        sessionFactory.setConfiguration(configuration);
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(mybatisTenantLineHandler()));
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(currentDbType()));
        return interceptor;
    }

    // 数据权限后续可参考：https://baomidou.com/plugins/data-permission/
//    @Bean
//    public DataPermissionInterceptor permissionInterceptor() {
//        return new DataPermissionInterceptor();
//    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        // 配置事务管理
        return new DataSourceTransactionManager(dynamicDataSource());
    }

    @Bean
    public TenantLineHandler mybatisTenantLineHandler() {
        return new CustomTenantHandler();
    }

    @Bean
    public MetaObjectHandler mybatisMetaObjectHandler() {
        return new EntityAutoFillHandler();
    }

    private DbType currentDbType() {
        return MYSQL.equals(databaseType) ? DbType.MYSQL : DbType.POSTGRE_SQL;
    }

}
