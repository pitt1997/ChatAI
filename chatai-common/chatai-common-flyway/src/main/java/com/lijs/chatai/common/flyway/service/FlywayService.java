package com.lijs.chatai.common.flyway.service;

import javax.sql.DataSource;

/**
 * @author ljs
 * @date 2024-12-13
 * @description
 */
public interface FlywayService {

    /**
     * 执行单个数据源
     */
    void migrateDataSource(DataSource ds);

    /**
     * 执行所有数据源
     */
    void migrate();

}
