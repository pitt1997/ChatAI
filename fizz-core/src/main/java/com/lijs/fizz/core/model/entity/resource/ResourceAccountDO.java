package com.lijs.fizz.core.model.entity.resource;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lijs.fizz.common.mybatis.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 资源账号表
 *
 * @TableName t_resource_account
 */
@Data
@TableName(value = "t_resource_account")
public class ResourceAccountDO extends BaseEntity implements Serializable {

    /**
     * 账号名称
     */
    private String name;

    /**
     * 资源ID
     */
    private String resourceId;

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