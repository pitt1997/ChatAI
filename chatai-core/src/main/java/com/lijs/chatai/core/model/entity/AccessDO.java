package com.lijs.chatai.core.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lijs.chatai.common.mybatis.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 访问控制表
 *
 * @TableName t_access
 */
//@Data
@TableName(value = "t_access")
public class AccessDO extends BaseEntity implements Serializable {

    /**
     * 资源ID
     */
    private String resourceId;

    /**
     * 用户组ID
     */
    private String userGroupId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }
}