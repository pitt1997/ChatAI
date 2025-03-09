package com.lijs.fizz.core.model.vo;

import com.lijs.fizz.core.model.entity.permission.PermissionDO;
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
