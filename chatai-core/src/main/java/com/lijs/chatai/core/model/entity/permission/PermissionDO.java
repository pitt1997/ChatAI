package com.lijs.chatai.core.model.entity.permission;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lijs.chatai.common.mybatis.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 功能权限表
 *
 * @TableName t_permission
 */
@Data
@TableName(value = "t_permission")
public class PermissionDO extends BaseEntity implements Serializable {

    /**
     * 权限点名称
     */
    private String name;

    /**
     * 权限点URL路由
     */
    private String route;

    /**
     * 权限点动作新增、删除、查看
     */
    private String action;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}