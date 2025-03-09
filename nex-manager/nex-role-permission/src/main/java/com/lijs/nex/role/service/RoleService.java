package com.lijs.nex.role.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lijs.nex.core.model.entity.permission.RoleDO;
import com.lijs.nex.core.model.vo.RoleVO;
import com.lijs.nex.role.model.pager.RolePage;
import com.lijs.nex.role.model.request.RoleRequest;

import java.util.List;

/**
 * @author pitt1
 * @description 针对表【t_role(系统角色表)】的数据库操作Service
 * @createDate 2025-01-02 15:26:56
 */
public interface RoleService extends IService<RoleDO> {

    RoleVO getRoleVOById(String id);

    IPage<RoleVO> getRolePage(RolePage page);

    RoleVO add(RoleRequest roleRequest);

    RoleVO update(RoleRequest roleRequest);

    RoleVO getRoleVO(RoleDO roleDO);

    boolean logicDeleteById(String id);

    boolean logicDeleteByIds(List<String> ids);
}
