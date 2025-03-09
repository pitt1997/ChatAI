package com.lijs.nex.role.service.impl;

import com.lijs.nex.common.base.constant.CommonConstants;
import com.lijs.nex.role.service.DataPermissionService;
import com.lijs.nex.role.service.RoleOrganizationService;
import com.lijs.nex.role.service.RoleResourceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ljs
 * @date 2025-01-09
 * @description
 */
@Service
public class DataPermissionServiceImpl implements DataPermissionService {

    @Autowired
    private RoleOrganizationService roleOrganizationService;

    @Autowired
    private RoleResourceGroupService roleResourceGroupService;

    @Override
    public List<String> getUserIds(String userId) {
        return null;
    }

    @Override
    public List<String> getOrgIds(String userId) {
        return roleOrganizationService.getPermissionOrganizationIds(userId);
    }

    @Override
    public List<String> getResIds(String userId) {
        return null;
    }

    @Override
    public List<String> getResGroupIds(String userId) {
        return roleResourceGroupService.getPermissionResourceGroupIds(userId);
    }

    @Override
    public boolean isSuperAdmin(String userId) {
        if (CommonConstants.ADMINISTRATOR.DEFAULT_ID.equals(userId)) {
            return true;
        }

        return false;
    }
}
