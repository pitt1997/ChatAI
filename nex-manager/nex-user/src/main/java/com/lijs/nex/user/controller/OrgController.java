package com.lijs.nex.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lijs.nex.common.base.constant.CommonConstants;
import com.lijs.nex.common.base.enums.ErrorCodeEnum;
import com.lijs.nex.common.base.exception.BusinessException;
import com.lijs.nex.common.base.response.BaseResponse;
import com.lijs.nex.common.base.utils.ResultUtils;
import com.lijs.nex.common.web.controller.BaseController;
import com.lijs.nex.core.model.vo.OrgVO;
import com.lijs.nex.user.model.pager.OrgPage;
import com.lijs.nex.user.model.request.OrgAddRequest;
import com.lijs.nex.user.model.request.OrgUpdateRequest;
import com.lijs.nex.user.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author author
 * @date 2025-01-04
 * @description
 */
@RestController
@RequestMapping("/org")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@Slf4j
public class OrgController extends BaseController {

    @Resource
    private OrganizationService organizationService;

    /**
     * 根据ID查询组织
     */
    @GetMapping("/{id}")
    public BaseResponse<OrgVO> getOrgVO(@PathVariable("id") String id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(organizationService.getOrgVOById(id));
    }

    @GetMapping("/page")
    public BaseResponse<IPage<OrgVO>> getOrgPage(@RequestBody OrgPage page, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(organizationService.getOrgPage(page));
    }

    /**
     * 新增组织
     */
    @PostMapping("/add")
    public BaseResponse<OrgVO> add(@RequestBody OrgAddRequest addRequest, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(organizationService.add(addRequest));
    }

    /**
     * 修改组织
     */
    @PutMapping("/update")
    public BaseResponse<OrgVO> update(@RequestBody OrgUpdateRequest updateRequest, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(organizationService.update(updateRequest));
    }

    @DeleteMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody List<String> selectIds, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.NO_AUTH);
        }
        if (selectIds == null || selectIds.size() == 0) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        if (selectIds.contains(CommonConstants.ADMINISTRATOR.DEFAULT_ID)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        boolean result = organizationService.removeByIds(selectIds);
        return ResultUtils.success(result);
    }

    @DeleteMapping("/deleteOrg")
    public BaseResponse<Boolean> deleteOrg(@RequestBody String id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.NO_AUTH);
        }
        if (CommonConstants.ADMINISTRATOR.DEFAULT_ID.equals(id)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        boolean result = organizationService.removeById(id);
        return ResultUtils.success(result);
    }

}
