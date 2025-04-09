package com.lijs.chatai.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lijs.chatai.core.model.entity.permission.RoleUserDO;

import java.util.List;

/**
 * @author pitt1
 * @description 针对表【t_role_user(系统角色和用户关联表)】的数据库操作Service
 * @createDate 2025-01-02 15:26:56
 */
public interface RoleUserService extends IService<RoleUserDO> {

    boolean assignRoleToUser(String roleId, List<String> userIds);

    boolean assignRoleToUser(String roleId, List<String> addUserIds, List<String> deleteUserIds);
}
