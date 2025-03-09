package com.lijs.nex.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lijs.nex.core.model.entity.permission.RoleOrganizationDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author pitt1
 * @description 针对表【t_role_organization(角色关联组织机构表)】的数据库操作Mapper
 * @createDate 2025-01-02 15:26:56
 * @Entity com.lijs.nex.core.entity.permission.RoleOrganizationDO
 */
public interface RoleOrganizationDAO extends BaseMapper<RoleOrganizationDO> {

    List<String> getPermissionOrganizationIds(@Param("userId") String userId);

}
