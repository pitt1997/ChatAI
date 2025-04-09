package com.lijs.chatai.core.model.vo;

import com.lijs.chatai.core.model.entity.idm.OrganizationDO;
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
