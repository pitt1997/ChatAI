package com.lijs.chatai.core.model.entity.idm;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lijs.chatai.common.mybatis.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户组表
 *
 * @TableName t_user_group
 */
@Data
@TableName(value = "t_user_group")
public class UserGroupDO extends BaseEntity implements Serializable {

    /**
     * 用户组名称
     */
    private String name;

    /**
     * 用户组父节点ID
     */
    private String parentId;

    /**
     * 用户组路径
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