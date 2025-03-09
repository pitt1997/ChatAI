package com.lijs.nex.core.model.entity.permission;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lijs.nex.common.mybatis.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色关联组织机构表
 *
 * @TableName t_role_organization
 */
@Data
@TableName(value = "t_role_organization")
public class RoleOrganizationDO extends BaseEntity implements Serializable {

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 组织机构ID
     */
    private String organizationId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}