package com.lijs.chatai.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijs.chatai.core.model.entity.permission.RoleOrganizationDO;

import java.util.List;

/**
 * @author pitt1
 * @description 针对表【t_role_organization(角色关联组织机构表)】的数据库操作Service
 * @createDate 2025-01-02 15:26:56
 */
public interface RoleOrganizationService extends IService<RoleOrganizationDO> {

    List<String> getPermissionOrganizationIds(String userId);

}
