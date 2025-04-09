package com.lijs.chatai.core.model.entity.permission;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lijs.chatai.common.mybatis.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色关联资源组表
 *
 * @TableName t_role_resource_group
 */
@Data
@TableName(value = "t_role_resource_group")
public class RoleResourceGroupDO extends BaseEntity implements Serializable {

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 资源组ID
     */
    private String resourceGroupId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}