package com.lijs.nex.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lijs.nex.core.model.dto.DataPermission;
import com.lijs.nex.core.model.entity.permission.RoleDO;
import com.lijs.nex.core.model.vo.RoleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author pitt1
 * @description 针对表【t_role(系统角色表)】的数据库操作Mapper
 * @createDate 2025-01-02 15:26:56
 * @Entity com.lijs.nex.core.entity.permission.RoleDO
 */
public interface RoleDAO extends BaseMapper<RoleDO> {

    RoleVO getRoleVOById(@Param("id") String id, @Param("dataPermission") DataPermission dataPermission);

    List<RoleVO> selectRoleList(@Param("query") RoleVO RoleVO, @Param("dataPermission") DataPermission dataPermission);

    IPage<RoleVO> getRolePage(@Param("page") Page<?> page, @Param("dataPermission") DataPermission dataPermission);

    int logicDeleteById(@Param("id") String id);

    int logicDeleteByIds(@Param("ids") List<String> ids);

}
