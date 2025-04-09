package com.lijs.chatai.core.model.vo;

import com.lijs.chatai.core.model.entity.idm.UserDO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ljs
 * @date 2025-01-06
 * @description
 */
@Data
public class UserVO extends UserDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户角色
     */
    private Integer userRole;

    /**
     * 组织机构ID
     */
    private String organizationName;

    public UserVO() {

    }


}
