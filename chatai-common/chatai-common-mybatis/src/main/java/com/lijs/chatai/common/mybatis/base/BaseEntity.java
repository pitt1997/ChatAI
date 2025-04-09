package com.lijs.chatai.common.mybatis.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lijs.chatai.common.base.utils.UUIDGenerator;
import com.lijs.chatai.common.base.session.SessionUserHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 基础数据对象，包含主键 ID、创建时间、更新时间等公共字段。
 *
 * @author ljs
 * @date 2024-12-19
 * @description
 */
public abstract class BaseEntity extends BaseEntityID {

    @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    private String tenantId;

    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * 创建对象时自动填充
     */
    public void autoFillForCreate() {
        Date currentDate = new Date();
        String userId = getCurrentUser();
        if (StringUtils.isBlank(this.getId())) {
            this.setId(UUIDGenerator.get32UUID());
        }
        this.setCreateUser(userId);
        this.setCreateTime(currentDate);
        this.autoFillForUpdate();
    }

    /**
     * 修改对象时自动填充
     */
    public void autoFillForUpdate() {
        Date currentDate = new Date();
        String userId = getCurrentUser();
        this.setUpdateUser(userId);
        this.setUpdateTime(currentDate);
    }

    private String getCurrentUser() {
        return SessionUserHelper.getUserId() != null ? SessionUserHelper.getUserId() : "";
    }

}
