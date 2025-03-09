package com.lijs.fizz.core.model.vo;

import com.lijs.fizz.core.model.entity.idm.OrganizationDO;
import lombok.Data;

/**
 * @author ljs
 * @date 2025-01-06
 * @description
 */
@Data
public class OrgVO extends OrganizationDO {

    private String id;

    private String name;

    private String code;

    private String parentId;

    private String path;

    private Integer status;

    private Integer isDelete;

    private String description;

}
