package com.lijs.fizz.core.model.entity.permission;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lijs.fizz.common.mybatis.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统角色表
 *
 * @TableName t_role
 */
@Data
@TableName(value = "t_role")
public class RoleDO extends BaseEntity implements Serializable {

    /**
     * 上级角色ID
     */
    private String parentId;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色全路径
     */
    private String path;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 是否删除 0-逻辑未删除 1-逻辑已删除
     */
    @TableLogic
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}