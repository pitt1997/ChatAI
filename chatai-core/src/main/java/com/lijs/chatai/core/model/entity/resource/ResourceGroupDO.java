package com.lijs.chatai.core.model.entity.resource;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lijs.chatai.common.mybatis.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 资源组表
 *
 * @TableName t_resource_group
 */
@Data
@TableName(value = "t_resource_group")
public class ResourceGroupDO extends BaseEntity implements Serializable {

    /**
     * 资源组名称
     */
    private String name;

    /**
     * 资源组父节点ID
     */
    private String parentId;

    /**
     * 资源组路径
     */
    private String path;

    /**
     * 资源组描述
     */
    private String description;

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