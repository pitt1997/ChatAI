package com.lijs.chatai.role.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色
 *
 * @author ljs
 * @date 2025-01-04
 * @description
 */
@Data
public class RoleBO implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;

    private String userPassword;
}
