package com.lijs.chatai.resource.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author ljs
 * @date 2025-01-04
 * @description
 */
@Data
public class ResAddRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;

    private String userPassword;
}
