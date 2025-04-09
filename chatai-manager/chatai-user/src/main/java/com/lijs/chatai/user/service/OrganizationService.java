package com.lijs.chatai.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lijs.chatai.core.model.entity.idm.OrganizationDO;
import com.lijs.chatai.core.model.vo.OrgVO;
import com.lijs.chatai.user.model.request.OrgAddRequest;
import com.lijs.chatai.user.model.request.OrgUpdateRequest;

/**
 * @author pitt1
 * @description 针对表【t_organization(组织机构表)】的数据库操作Service
 * @createDate 2025-01-02 15:26:56
 */
public interface OrganizationService extends IService<OrganizationDO> {

    OrgVO getOrgVOById(String id);

    IPage<OrgVO> getOrgPage(Page<?> page);

    OrgVO add(OrgAddRequest addRequest);

    OrgVO update(OrgUpdateRequest updateRequest);
}
