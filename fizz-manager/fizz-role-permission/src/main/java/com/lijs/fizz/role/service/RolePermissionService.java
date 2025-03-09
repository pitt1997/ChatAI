package com.lijs.fizz.role.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lijs.fizz.core.model.entity.permission.RolePermissionDO;
import com.lijs.fizz.core.model.vo.PermissionVO;

import java.util.List;

/**
 * @author pitt1
 * @description 针对表【t_role_permission(角色关联权限表)】的数据库操作Service
 * @createDate 2025-01-02 15:26:56
 */
public interface RolePermissionService extends IService<RolePermissionDO> {

    List<PermissionVO> getRolePermissions(String roleId);

    Boolean saveRolePermission(String roleId, String permissionIds);

    List<Tree<PermissionVO>> getRolePermissionsMenuTree(String roleId);
}
