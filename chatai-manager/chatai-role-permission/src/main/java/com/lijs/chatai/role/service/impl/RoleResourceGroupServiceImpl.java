package com.lijs.chatai.role.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijs.chatai.role.service.RoleResourceGroupService;
import com.lijs.chatai.core.model.entity.permission.RoleResourceGroupDO;
import com.lijs.chatai.role.dao.RoleResourceGroupDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author pitt1
* @description 针对表【t_role_resource_group(角色关联资源组表)】的数据库操作Service实现
* @createDate 2025-01-02 15:26:56
*/
@Service
public class RoleResourceGroupServiceImpl extends ServiceImpl<RoleResourceGroupDAO, RoleResourceGroupDO>
        implements RoleResourceGroupService {

    @Autowired
    private RoleResourceGroupDAO roleResourceGroupDAO;

    @Override
    public List<String> getPermissionResourceGroupIds(String userId) {
        return roleResourceGroupDAO.getPermissionResourceGroupIds(userId);
    }

}
