package com.lijs.nex.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lijs.nex.core.model.entity.permission.RoleResourceGroupDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author pitt1
 * @description 针对表【t_role_resource_group(角色关联资源组表)】的数据库操作Mapper
 * @createDate 2025-01-02 15:26:56
 * @Entity com.lijs.nex.core.entity.permission.RoleResourceGroupDO
 */
public interface RoleResourceGroupDAO extends BaseMapper<RoleResourceGroupDO> {

    List<String> getPermissionResourceGroupIds(@Param("userId") String userId);

}
