//package com.lijs.chatai.common.mybatis.handler;
//
//import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
//import com.lijs.chatai.common.base.tenant.TenantContextHolder;
//import net.sf.jsqlparser.expression.Expression;
//import net.sf.jsqlparser.expression.StringValue;
//
///**
// * @author lijs
// * @date 2025-01-11
// * @description
// */
//public class CustomTenantHandler implements TenantLineHandler {
//
//    @Override
//    public Expression getTenantId() {
//        // 返回租户 ID，这里可以动态获取当前租户 ID
//        String tenantId = TenantContextHolder.get();
//        // TODO 租户为空时怎么办
//        return new StringValue(tenantId);
//    }
//
//    /**
//     * 获取租户字段名
//     * 默认字段名叫: tenant_id
//     *
//     * @return 租户字段名
//     */
//    @Override
//    public String getTenantIdColumn() {
//        return "tenant_id";
//    }
//
//    /**
//     * 根据表名判断是否忽略拼接多租户条件
//     * 默认都要进行解析并拼接多租户条件
//     *
//     * @param tableName 表名
//     * @return 是否忽略, true:表示忽略，false:需要解析并拼接多租户条件
//     */
//    @Override
//    public boolean ignoreTable(String tableName) {
//        // 指定哪些表不应用租户条件
//        // return !"sys_user".equalsIgnoreCase(tableName);
//        // 默认都忽略
//        return true;
//    }
//
//    /**
//     * 忽略插入租户字段逻辑
//     *
//     * @param columns        插入字段
//     * @param tenantIdColumn 租户 ID 字段
//     * @return
//     */
////    @Override
////    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
////        return columns.stream().map(Column::getColumnName).anyMatch(i -> i.equalsIgnoreCase(tenantIdColumn));
////    }
//}