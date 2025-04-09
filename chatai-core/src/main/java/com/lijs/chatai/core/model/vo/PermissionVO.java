package com.lijs.chatai.core.model.vo;

import com.lijs.chatai.core.model.entity.permission.PermissionDO;
import lombok.Data;

/**
 * @author ljs
 * @date 2025-01-06
 * @description
 */
@Data
public class PermissionVO extends PermissionDO {

    private String id;

    private String name;

}
