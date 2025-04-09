package com.lijs.chatai.core.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author ljs
 * @date 2025-01-14
 * @description
 */
@Data
public class DataPermission {

    private String currentUserId;

    private List<String> organizationIds;

    private List<String> resourceGroupIds;

}
