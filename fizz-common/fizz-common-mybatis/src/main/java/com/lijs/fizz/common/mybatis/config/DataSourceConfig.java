package com.lijs.fizz.common.mybatis.config;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 数据源配置（支持多数据源）
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceConfig {

    private HikariPoolConfig hikari = new HikariPoolConfig();

    private ClassLoader classLoader;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 是否生成随机数据源名称
     */
    private boolean generateUniqueName = true;

    /**
     * 要使用的连接池实现的完全限定名称。默认情况下，从类路径自动检测
     */
    private Class<? extends DataSource> type;

    /**
     * JDBC 驱动的完全限定名称。根据 URL 自动检测
     */
    private String driverClassName;

    /**
     * 数据库的 JDBC URL
     */
    private String url;

    /**
     * 数据库的登录用户名
     */
    private String username;

    /**
     * 数据库的登录密码
     */
    private String password;

    /**
     * 数据源的 JNDI 位置。当设置时，将忽略类、URL、用户名和密码。
     */
    private String jndiName;

    /**
     * 确定是否应根据可用的 DDL 和 DML 脚本执行 DataSource 初始化的模式。
     */
    private DataSourceInitializationMode initializationMode = DataSourceInitializationMode.EMBEDDED;

    /**
     * 在 DDL 或 DML 脚本中使用
     */
    private String platform = "all";

    /**
     * 模式（DDL）脚本资源引用。
     */
    private List<String> schema;

    /**
     * 执行 DDL 脚本的数据库用户名（如果不同）。
     */
    private String schemaUsername;

    /**
     * 执行 DDL 脚本的数据库密码（如果不同）。
     */
    private String schemaPassword;

    /**
     * 数据（DML）脚本资源引用。
     */
    private List<String> data;

    /**
     * 执行 DML 脚本的数据库用户名（如果不同）。
     */
    private String dataUsername;

    /**
     * 执行 DML 脚本的数据库密码（如果不同）。
     */
    private String dataPassword;

    /**
     * 在初始化数据库时是否在发生错误时停止。
     */
    private boolean continueOnError = false;

    /**
     * SQL 初始化脚本中的语句分隔符。
     */
    private String separator = ";";

    /**
     * SQL 脚本编码。
     */
    private Charset sqlScriptEncoding;

    private final EmbeddedDatabaseConnection embeddedDatabaseConnection = EmbeddedDatabaseConnection.NONE;

    private org.springframework.boot.autoconfigure.jdbc.DataSourceProperties.Xa xa = new org.springframework.boot.autoconfigure.jdbc.DataSourceProperties.Xa();

    private String uniqueName;


    public HikariPoolConfig getHikari() {
        return hikari;
    }

    public void setHikari(HikariPoolConfig hikari) {
        this.hikari = hikari;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGenerateUniqueName() {
        return this.generateUniqueName;
    }

    public void setGenerateUniqueName(boolean generateUniqueName) {
        this.generateUniqueName = generateUniqueName;
    }

    public Class<? extends DataSource> getType() {
        return this.type;
    }

    public void setType(Class<? extends DataSource> type) {
        this.type = type;
    }

    /**
     * 返回配置的驱动或如果没有配置则返回 {@code null}。
     *
     * @return 配置的驱动
     */
    public String getDriverClassName() {
        return this.driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }


    private boolean driverClassIsLoadable() {
        try {
            ClassUtils.forName(this.driverClassName, null);
            return true;
        } catch (UnsupportedClassVersionError ex) {
            // 驱动库使用较新的 JDK 编译，传播错误
            throw ex;
        } catch (Throwable ex) {
            return false;
        }
    }

    /**
     * 返回配置的 URL 或如果没有配置则返回 {@code null}。
     *
     * @return 配置的 URL
     */
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    /**
     * 根据此配置确定要使用的名称。
     *
     * @return 要使用的数据库名称或 {@code null}
     * @since 2.0.0
     */
    public String determineDatabaseName() {
        if (this.generateUniqueName) {
            if (this.uniqueName == null) {
                this.uniqueName = UUID.randomUUID().toString();
            }
            return this.uniqueName;
        }
        if (StringUtils.hasLength(this.name)) {
            return this.name;
        }
        if (this.embeddedDatabaseConnection != EmbeddedDatabaseConnection.NONE) {
            return "testdb";
        }
        return null;
    }

    /**
     * 返回配置的用户名或如果没有配置则返回 {@code null}。
     *
     * @return 配置的用户名
     */
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * 返回配置的密码或如果没有配置则返回 {@code null}。
     *
     * @return 配置的密码
     */
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getJndiName() {
        return this.jndiName;
    }

    /**
     * 允许通过 JNDI 管理 DataSource，并通过 JNDI 查找获得。使用 JNDI 查找时，将忽略 {@code URL}、{@code driverClassName}、{@code username} 和 {@code password} 字段。
     *
     * @param jndiName JNDI 名称
     */
    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public DataSourceInitializationMode getInitializationMode() {
        return this.initializationMode;
    }

    public void setInitializationMode(DataSourceInitializationMode initializationMode) {
        this.initializationMode = initializationMode;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public List<String> getSchema() {
        return this.schema;
    }

    public void setSchema(List<String> schema) {
        this.schema = schema;
    }

    public String getSchemaUsername() {
        return this.schemaUsername;
    }

    public void setSchemaUsername(String schemaUsername) {
        this.schemaUsername = schemaUsername;
    }

    public String getSchemaPassword() {
        return this.schemaPassword;
    }

    public void setSchemaPassword(String schemaPassword) {
        this.schemaPassword = schemaPassword;
    }

    public List<String> getData() {
        return this.data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getDataUsername() {
        return this.dataUsername;
    }

    public void setDataUsername(String dataUsername) {
        this.dataUsername = dataUsername;
    }

    public String getDataPassword() {
        return this.dataPassword;
    }

    public void setDataPassword(String dataPassword) {
        this.dataPassword = dataPassword;
    }

    public boolean isContinueOnError() {
        return this.continueOnError;
    }

    public void setContinueOnError(boolean continueOnError) {
        this.continueOnError = continueOnError;
    }

    public String getSeparator() {
        return this.separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public Charset getSqlScriptEncoding() {
        return this.sqlScriptEncoding;
    }

    public void setSqlScriptEncoding(Charset sqlScriptEncoding) {
        this.sqlScriptEncoding = sqlScriptEncoding;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    public org.springframework.boot.autoconfigure.jdbc.DataSourceProperties.Xa getXa() {
        return this.xa;
    }

    public void setXa(org.springframework.boot.autoconfigure.jdbc.DataSourceProperties.Xa xa) {
        this.xa = xa;
    }

    /**
     * XA 特定数据源设置。
     */
    public static class Xa {

        /**
         * XA 数据源的完全限定名称。
         */
        private String dataSourceClassName;

        /**
         * 传递给 XA 数据源的属性。
         */
        private Map<String, String> properties = new LinkedHashMap<>();

        public String getDataSourceClassName() {
            return this.dataSourceClassName;
        }

        public void setDataSourceClassName(String dataSourceClassName) {
            this.dataSourceClassName = dataSourceClassName;
        }

        public Map<String, String> getProperties() {
            return this.properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }

    }

    static class DataSourceBeanCreationException extends BeanCreationException {

        private final org.springframework.boot.autoconfigure.jdbc.DataSourceProperties properties;

        private final EmbeddedDatabaseConnection connection;

        DataSourceBeanCreationException(String message, org.springframework.boot.autoconfigure.jdbc.DataSourceProperties properties,
                                        EmbeddedDatabaseConnection connection) {
            super(message);
            this.properties = properties;
            this.connection = connection;
        }

        org.springframework.boot.autoconfigure.jdbc.DataSourceProperties getProperties() {
            return this.properties;
        }

        EmbeddedDatabaseConnection getConnection() {
            return this.connection;
        }

    }
}
