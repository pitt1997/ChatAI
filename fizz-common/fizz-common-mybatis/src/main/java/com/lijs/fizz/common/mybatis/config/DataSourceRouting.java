package com.lijs.fizz.common.mybatis.config;

import com.lijs.fizz.common.base.exception.DataSourceRoutingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 注意：切换数据源必须在调用任何服务方法之前完成，确保在事务开启之前进行。
 * 这是为了保证所有数据库操作都在正确的数据源上下文中执行，避免引发数据一致性和完整性问题。
 *
 * @author ljs
 * @date 2024-10-06
 * @description 数据源路由逻辑
 */
public class DataSourceRouting extends AbstractRoutingDataSource {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceRouting.class);

    public static final String PRIMARY_DB = "primary";

    public DataSource primaryDatasource;

    public DataSourceRouting(boolean lenientFallback) {
        super.setLenientFallback(lenientFallback);
    }

    /**
     * 如果不希望数据源在启动配置时就加载好，可以定制这个方法，从任何你希望的地方读取并返回数据源
     * 比如从数据库、文件、外部接口等读取数据源信息，并最终返回一个DataSource实现类对象即可
     */
    @Override
    protected DataSource determineTargetDataSource() {
        try {
            return super.determineTargetDataSource();
        } catch (Exception e) {
            throw new DataSourceRoutingException("获取目标数据源失败: " + e.getMessage());
        }
    }

    /**
     * 如果希望所有数据源在启动配置时就加载好，这里通过设置数据源Key值来切换数据，定制这个方法
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String datasourceKey = getDatasourceKey();
        logger.debug("当前数据源切换至: {}", datasourceKey);
        return datasourceKey;
    }

    private String getDatasourceKey() {
        return PRIMARY_DB;
    }

    /**
     * 设置默认数据源
     */
    public void setDefaultDataSource(Object defaultDataSource) {
        setPrimaryDatasource((DataSource) defaultDataSource);
        super.setDefaultTargetDataSource(defaultDataSource);
    }

    /**
     * 设置数据源
     */
    public void setDataSources(Map<Object, Object> dataSources) {
        super.setTargetDataSources(dataSources);
    }

    public DataSource getPrimaryDatasource() {
        return primaryDatasource;
    }

    public void setPrimaryDatasource(DataSource primaryDatasource) {
        this.primaryDatasource = primaryDatasource;
    }

    public DataSource getDataSource(String tenantId) {
        return getResolvedDataSources().get(tenantId);
    }

}
