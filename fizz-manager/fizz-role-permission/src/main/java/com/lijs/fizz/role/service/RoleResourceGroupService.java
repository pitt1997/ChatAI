package com.lijs.fizz.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijs.fizz.core.model.entity.permission.RoleResourceGroupDO;

import java.util.List;

/**
 * @author pitt1
 * @description 针对表【t_role_resource_group(角色关联资源组表)】的数据库操作Service
 * @createDate 2025-01-02 15:26:56
 */
public interface RoleResourceGroupService extends IService<RoleResourceGroupDO> {

    List<String> getPermissionResourceGroupIds(String userId);

}
