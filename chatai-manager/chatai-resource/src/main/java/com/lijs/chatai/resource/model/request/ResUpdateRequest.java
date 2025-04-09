package com.lijs.chatai.resource.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author ljs
 * @date 2025-01-04
 * @description
 */
@Data
public class ResUpdateRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String mobile;

}
