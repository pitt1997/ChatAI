package com.lijs.chatai.core.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lijs.chatai.common.mybatis.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户账号表
 *
 * @TableName t_user_account
 */
@Data
@TableName(value = "t_user_account")
public class UserAccountDO extends BaseEntity implements Serializable {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 资源账号ID
     */
    private String accountId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}