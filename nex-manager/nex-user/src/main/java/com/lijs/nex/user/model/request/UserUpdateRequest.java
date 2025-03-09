package com.lijs.nex.user.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ljs
 * @date 2025-01-22
 * @description
 */
@Data
public class UserUpdateRequest extends UserAddRequest implements Serializable {

    private String id;

    /**
     * 用户名
     */
    private String name;

    private String nickname;

    /**
     * 中文名称
     */
    private String cnName;

    private String organizationId;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    private String password;

    private String status;

    private String description;

}
