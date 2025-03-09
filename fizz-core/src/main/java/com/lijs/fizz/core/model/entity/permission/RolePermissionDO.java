package com.lijs.fizz.core.model.entity.permission;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lijs.fizz.common.mybatis.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色关联权限表
 *
 * @TableName t_role_permission
 */
@Data
@TableName(value = "t_role_permission")
public class RolePermissionDO extends BaseEntity implements Serializable {

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 权限点ID
     */
    private String permissionId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}