package com.lijs.nex.core.model.entity.permission;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lijs.nex.common.mybatis.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统角色和用户关联表
 *
 * @TableName t_role_user
 */
@Data
@TableName(value = "t_role_user")
public class RoleUserDO extends BaseEntity implements Serializable {

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 用户ID
     */
    private String userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}