package com.lijs.chatai.role.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijs.chatai.role.service.RoleUserService;
import com.lijs.chatai.core.model.entity.permission.RoleUserDO;
import com.lijs.chatai.role.dao.RoleUserDAO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author pitt1
* @description 针对表【t_role_user(系统角色和用户关联表)】的数据库操作Service实现
* @createDate 2025-01-02 15:26:56
*/
@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserDAO, RoleUserDO>
        implements RoleUserService {

    @Resource
    private RoleUserDAO roleUserDAO;

    @Override
    public boolean assignRoleToUser(String roleId, List<String> userIds) {
        // 校验角色是否存在
        List<RoleUserDO> list = new ArrayList<>();
        for (String userId : userIds) {
            RoleUserDO roleUserDO = new RoleUserDO();
            roleUserDO.setRoleId(roleId);
            roleUserDO.setUserId(userId);
            list.add(roleUserDO);
        }

        return saveBatch(list);
    }

    @Override
    public boolean assignRoleToUser(String roleId, List<String> addUserIds, List<String> deleteUserIds) {
        // 校验角色是否存在
        List<RoleUserDO> deleteList = new ArrayList<>();
        for (String userId : deleteUserIds) {
            RoleUserDO roleUserDO = new RoleUserDO();
            roleUserDO.setRoleId(roleId);
            roleUserDO.setUserId(userId);
            deleteList.add(roleUserDO);
        }
        removeByIds(deleteList);

        List<RoleUserDO> addList = new ArrayList<>();
        for (String userId : addUserIds) {
            RoleUserDO roleUserDO = new RoleUserDO();
            roleUserDO.setRoleId(roleId);
            roleUserDO.setUserId(userId);
            addList.add(roleUserDO);
        }
        saveBatch(addList);

        return true;
    }
}
