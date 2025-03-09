package com.lijs.fizz.role.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijs.fizz.role.service.RoleOrganizationService;
import com.lijs.fizz.core.model.entity.permission.RoleOrganizationDO;
import com.lijs.fizz.role.dao.RoleOrganizationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author pitt1
* @description 针对表【t_role_organization(角色关联组织机构表)】的数据库操作Service实现
* @createDate 2025-01-02 15:26:56
*/
@Service
public class RoleOrganizationServiceImpl extends ServiceImpl<RoleOrganizationDAO, RoleOrganizationDO>
        implements RoleOrganizationService {

    @Autowired
    private RoleOrganizationDAO roleOrganizationDAO;

    @Override
    public List<String> getPermissionOrganizationIds(String userId) {
        return roleOrganizationDAO.getPermissionOrganizationIds(userId);
    }
}
