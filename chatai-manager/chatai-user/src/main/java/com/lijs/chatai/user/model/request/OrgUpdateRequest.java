package com.lijs.chatai.user.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ljs
 * @date 2025-01-22
 * @description
 */
@Data
public class OrgUpdateRequest implements Serializable {

    private String id;

    /**
     * 名称
     */
    private String name;
    /**
     * 父节点ID
     */
    private String parentId;

    /**
     * 父节点名称
     */
    private String parentName;

    /**
     * 描述
     */
    private String description;

}
