package com.lijs.chatai.core.model.vo;

import com.lijs.chatai.core.model.entity.permission.RoleDO;
import lombok.Data;

/**
 * @author ljs
 * @date 2025-01-06
 * @description
 */
@Data
public class RoleVO extends RoleDO {

    private String id;

    private String name;

    private String description;

}
