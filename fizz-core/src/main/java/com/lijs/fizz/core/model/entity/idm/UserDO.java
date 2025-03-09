package com.lijs.fizz.core.model.entity.idm;

import com.baomidou.mybatisplus.annotation.*;
import com.lijs.fizz.common.mybatis.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户表
 *
 * @TableName t_user
 */
@Data
@TableName(value = "t_user")
public class UserDO extends BaseEntity implements Serializable {

    /**
     * 用户名
     */
    private String name;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 中文名称
     */
    private String cnName;

    /**
     * 密码
     */
    private String password;

    /**
     * 组织机构ID
     */
    private String organizationId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 描述
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

    /**
     * 头像
     */
    private byte[] icon;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}