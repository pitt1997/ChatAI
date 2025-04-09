package com.lijs.chatai.role.service.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijs.chatai.core.model.vo.PermissionVO;
import com.lijs.chatai.role.service.RolePermissionService;
import com.lijs.chatai.core.model.entity.permission.RolePermissionDO;
import com.lijs.chatai.role.dao.RolePermissionDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author pitt1
* @description 针对表【t_role_permission(角色关联权限表)】的数据库操作Service实现
* @createDate 2025-01-02 15:26:56
*/
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionDAO, RolePermissionDO>
        implements RolePermissionService {

    @Override
    public List<PermissionVO> getRolePermissions(String roleId) {
        return null;
    }

    @Override
    public Boolean saveRolePermission(String roleId, String permissionIds) {
        return null;
    }

    @Override
    public List<Tree<PermissionVO>> getRolePermissionsMenuTree(String roleId) {
        return null;
    }
}
