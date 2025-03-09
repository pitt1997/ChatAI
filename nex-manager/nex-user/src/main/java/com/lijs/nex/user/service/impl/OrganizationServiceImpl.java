package com.lijs.nex.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lijs.nex.common.base.enums.ErrorCodeEnum;
import com.lijs.nex.common.base.enums.StatusEnum;
import com.lijs.nex.common.base.exception.BusinessException;
import com.lijs.nex.common.web.service.BaseService;
import com.lijs.nex.core.model.dto.DataPermission;
import com.lijs.nex.core.model.entity.idm.OrganizationDO;
import com.lijs.nex.core.model.vo.OrgVO;
import com.lijs.nex.user.dao.OrganizationDAO;
import com.lijs.nex.user.model.request.OrgAddRequest;
import com.lijs.nex.user.model.request.OrgUpdateRequest;
import com.lijs.nex.user.service.OrganizationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author pitt1
 * @description 针对表【t_organization(组织机构表)】的数据库操作Service实现
 * @createDate 2025-01-02 15:26:56
 */
@Service
public class OrganizationServiceImpl extends BaseService<OrganizationDAO, OrganizationDO>
        implements OrganizationService {

    @Resource
    private OrganizationDAO organizationDAO;

    @Override
    public OrgVO getOrgVOById(String id) {
        DataPermission dataPermission = new DataPermission();
        return organizationDAO.getOrgVOById(id, dataPermission);
    }

    @Override
    public IPage<OrgVO> getOrgPage(Page<?> page) {
        DataPermission dataPermission = new DataPermission();
        return organizationDAO.getOrgPage(page, dataPermission);
    }

    @Override
    public OrgVO add(OrgAddRequest addRequest) {
        checkOrgAdd(addRequest);
        OrganizationDO organizationDO = new OrganizationDO();
        organizationDO.setName(addRequest.getName());
        organizationDO.setParentId(addRequest.getParentId());
        organizationDO.setPath(addRequest.getParentName());
        organizationDO.setDescription(addRequest.getDescription());
        organizationDO.setStatus(StatusEnum.NORMAL.getCode());
        save(organizationDO);
        return getSafetyOrgVO(organizationDO);
    }

    @Override
    public OrgVO update(OrgUpdateRequest updateRequest) {
        checkOrgUpdate(updateRequest);
        OrganizationDO organizationDO = new OrganizationDO();
        organizationDO.setId(updateRequest.getId());
        organizationDO.setName(updateRequest.getName());
        organizationDO.setParentId(updateRequest.getParentId());
        organizationDO.setPath(updateRequest.getParentName());
        organizationDO.setDescription(updateRequest.getDescription());
        updateById(organizationDO);
        return getSafetyOrgVO(organizationDO);
    }

    public OrgVO getSafetyOrgVO(OrganizationDO originOrg) {
        if (originOrg == null) {
            return null;
        }

        OrgVO safetyOrg = new OrgVO();
        safetyOrg.setId(originOrg.getId());
        safetyOrg.setName(originOrg.getName());
        return safetyOrg;
    }

    public void checkOrgAdd(OrgAddRequest orgAddRequest) {
        String name = orgAddRequest.getName();
        String parentId = orgAddRequest.getParentId();
        // 校验
        if (StringUtils.isAnyBlank(name, parentId)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "参数为空");
        }
        // 账户不能包含特殊字符
        if (findSpecialChar(name)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "组织名称不能包含特殊字符");
        }
        // 组织已经存在 不能重复
        OrganizationDO parentOrg = organizationDAO.selectById(parentId);
        if (parentOrg == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "父节点不存在");
        }
        // 组织全路径
        orgAddRequest.setParentName(parentOrg.getPath() + "/" + name);
        // 组织已经存在 不能重复
        QueryWrapper<OrganizationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        queryWrapper.eq("parent_id", parentId);
        long count = organizationDAO.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "组织不能重复");
        }
    }

    private void checkOrgUpdate(OrgUpdateRequest orgUpdateRequest) {
        String orgId = orgUpdateRequest.getId();
        String name = orgUpdateRequest.getName();
        String parentId = orgUpdateRequest.getParentId();
        if (StringUtils.isEmpty(orgId)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        OrganizationDO oldOrg = this.getById(orgId);
        if (oldOrg == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_NULL_ERROR);
        }
        if (StringUtils.isNotEmpty(name)) {
            if (findSpecialChar(name)) {
                throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "组织名称不能包含特殊字符");
            }
            // 组织已经存在 不能重复
            QueryWrapper<OrganizationDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", name);
            queryWrapper.eq("parent_id", parentId);
            long count = organizationDAO.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "组织不能重复");
            }
            // 组织已经存在 不能重复
            OrganizationDO parentOrg = organizationDAO.selectById(parentId);
            if (parentOrg == null) {
                throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "父节点不存在");
            }
            // 组织全路径
            orgUpdateRequest.setParentName(parentOrg.getPath() + "/" + name);
        }
    }
}
