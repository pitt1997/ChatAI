package com.lijs.chatai.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lijs.chatai.core.model.dto.DataPermission;
import com.lijs.chatai.core.model.entity.idm.OrganizationDO;
import com.lijs.chatai.core.model.vo.OrgVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author pitt1
 * @description 针对表【t_organization(组织机构表)】的数据库操作Mapper
 * @createDate 2025-01-02 15:26:56
 * @Entity com.lijs.chatai.core.entity.idm.OrganizationDO
 */
public interface OrganizationDAO extends BaseMapper<OrganizationDO> {

    OrgVO getOrgVOById(String id, @Param("dataPermission") DataPermission dataPermission);

    IPage<OrgVO> getOrgPage(Page<?> page, @Param("dataPermission") DataPermission dataPermission);
}
