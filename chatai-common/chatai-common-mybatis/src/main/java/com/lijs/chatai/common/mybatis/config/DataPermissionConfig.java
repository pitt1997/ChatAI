package com.lijs.chatai.common.mybatis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "platform.data-permission")
public class DataPermissionConfig {

    // t_user -> < org_id -> "org_id in ?" >
    // t_organization -> < id -> "org_id in ?" >
    private List<TablePermission> tables;

    public List<TablePermission> getTables() {
        return tables;
    }

    public void setTables(List<TablePermission> tables) {
        this.tables = tables;
    }

    public static class TablePermission {
        /**
         * 表名称
         */
        private String name;
        /**
         * 表字段和字段动态条件映射
         */
        private Map<String, String> conditions; // 字段名到条件表达式的映射

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, String> getConditions() {
            return conditions;
        }

        public void setConditions(Map<String, String> conditions) {
            this.conditions = conditions;
        }
    }

}
