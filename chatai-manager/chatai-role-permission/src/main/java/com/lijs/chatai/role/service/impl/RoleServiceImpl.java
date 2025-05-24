package com.lijs.chatai.role.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijs.chatai.common.base.enums.StatusEnum;
import com.lijs.chatai.core.model.dto.DataPermission;
import com.lijs.chatai.core.model.entity.permission.RoleDO;
import com.lijs.chatai.core.model.vo.RoleVO;
import com.lijs.chatai.role.dao.RoleDAO;
import com.lijs.chatai.role.model.pager.RolePage;
import com.lijs.chatai.role.model.request.RoleRequest;
import com.lijs.chatai.role.service.RoleService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @author pitt1
 * @description 针对表【t_role(系统角色表)】的数据库操作Service实现
 * @createDate 2025-01-02 15:26:56
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleDAO, RoleDO>
        implements RoleService {

    @Resource
    private RoleDAO roleDAO;

    @Override
    public RoleVO getRoleVOById(String id) {
        DataPermission dataPermission = new DataPermission();
        return roleDAO.getRoleVOById(id, dataPermission);
    }

    @Override
    public IPage<RoleVO> getRolePage(RolePage page) {
        DataPermission dataPermission = new DataPermission();
        return roleDAO.getRolePage(page, dataPermission);
    }

    @Override
    public RoleVO add(RoleRequest roleRequest) {
        // 校验
        checkRoleAdd(roleRequest);
        RoleDO roleDO = new RoleDO();
        roleDO.setName(roleRequest.getName());
        roleDO.setParentId(roleRequest.getParentId());
        // roleDO.setPath();
        roleDO.setStatus(StatusEnum.NORMAL.getCode());
        save(roleDO);
        return getRoleVO(roleDO);
    }

    @Override
    public RoleVO update(RoleRequest roleRequest) {
        // 校验
        checkRoleUpdate(roleRequest);
        RoleDO roleDO = new RoleDO();
        roleDO.setId(roleRequest.getId());
        roleDO.setName(roleRequest.getName());
        roleDO.setParentId(roleRequest.getParentId());
        // roleDO.setPath();
        roleDO.setStatus(StatusEnum.NORMAL.getCode());
        updateById(roleDO);
        return getRoleVO(roleDO);
    }

    @Override
    public RoleVO getRoleVO(RoleDO roleDO) {
        RoleVO roleVO = new RoleVO();
        roleVO.setId(roleDO.getId());
        roleVO.setName(roleDO.getName());
        roleVO.setStatus(roleDO.getStatus());
        roleVO.setParentId(roleDO.getParentId());
        roleVO.setPath(roleDO.getPath());
        return roleVO;
    }

    @Override
    public boolean logicDeleteById(String id) {
        return roleDAO.logicDeleteById(id) > 0;
    }

    @Override
    public boolean logicDeleteByIds(List<String> ids) {
        return roleDAO.logicDeleteByIds(ids) > 0;
    }

    private void checkRoleUpdate(RoleRequest roleRequest) {
    }

    private void checkRoleAdd(RoleRequest roleRequest) {
    }
}
