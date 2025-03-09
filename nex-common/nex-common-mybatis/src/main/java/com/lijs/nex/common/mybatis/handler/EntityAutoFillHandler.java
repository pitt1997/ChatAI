package com.lijs.nex.common.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lijs.nex.common.base.enums.DeletedEnum;
import com.lijs.nex.common.base.enums.StatusEnum;
import com.lijs.nex.common.base.session.SessionUserHelper;
import com.lijs.nex.common.base.tenant.TenantContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * MyBatis-Plus 提供用于实际处理自动填充的逻，它根据实体类中的字段和注解规则，设置具体的填充值。
 * 在实体上增加 @TableField(value = "xxx", fill = FieldFill.INSERT) 则可自动填充
 */
public class EntityAutoFillHandler implements MetaObjectHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void insertFill(MetaObject metaObject) {
        Date date = new Date();
        String operator = getCurrentLoginUser();
        String tenantId = TenantContextHolder.get();
        if (metaObject.hasSetter("createTime") && getFieldValByName("createTime", metaObject) == null) {
            setFieldValByName("createTime", date, metaObject);
        }
        if (metaObject.hasSetter("updateTime") && getFieldValByName("updateTime", metaObject) == null) {
            setFieldValByName("updateTime", date, metaObject);
        }
        if (metaObject.hasSetter("createUser") && getFieldValByName("createUser", metaObject) == null) {
            if (StringUtils.isNotBlank(operator)) {
                setFieldValByName("createUser", operator, metaObject);
            }
        }
        if (metaObject.hasSetter("updateUser") && getFieldValByName("updateUser", metaObject) == null) {
            if (StringUtils.isNotBlank(operator)) {
                setFieldValByName("updateUser", operator, metaObject);
            }
        }
        if (metaObject.hasSetter("tenantId") && getFieldValByName("tenantId", metaObject) == null) {
            setFieldValByName("tenantId", tenantId, metaObject);
        }
        if (metaObject.hasSetter("status") && getFieldValByName("status", metaObject) == null) {
            setFieldValByName("status", StatusEnum.NORMAL.getCode(), metaObject);
        }
        if (metaObject.hasSetter("isDelete") && getFieldValByName("isDelete", metaObject) == null) {
            setFieldValByName("isDelete", DeletedEnum.NOT_DELETED.getCode(), metaObject);
        }
    }

    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter("updateTime")) {
            setFieldValByName("updateTime", new Date(), metaObject);
        }
        String operator = getCurrentLoginUser();
        if (metaObject.hasSetter("updateUser")) {
            if (StringUtils.isNotBlank(operator)) {
                setFieldValByName("updateUser", operator, metaObject);
            }
        }
    }

    private String getCurrentLoginUser() {
        String operator = null;
        try {
            operator = SessionUserHelper.getUserId();
        } catch (Exception e) {
            logger.debug("get current user info error:{}", e.getMessage());
        }

        return operator;
    }

}
