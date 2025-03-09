package com.lijs.fizz.role.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色新增
 *
 * @author ljs
 * @date 2025-01-04
 * @description
 */
@Data
public class RoleRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String id;

    /**
     * 用户名
     */
    private String name;

    private String parentId;

}
