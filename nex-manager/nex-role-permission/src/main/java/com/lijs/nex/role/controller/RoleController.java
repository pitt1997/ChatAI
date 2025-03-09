package com.lijs.nex.role.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lijs.nex.common.base.constant.CommonConstants;
import com.lijs.nex.common.base.enums.ErrorCodeEnum;
import com.lijs.nex.common.base.exception.BusinessException;
import com.lijs.nex.common.base.response.BaseResponse;
import com.lijs.nex.common.base.utils.ResultUtils;
import com.lijs.nex.common.web.controller.BaseController;
import com.lijs.nex.core.api.client.UserApiClient;
import com.lijs.nex.core.model.entity.idm.UserDO;
import com.lijs.nex.core.model.vo.RoleVO;
import com.lijs.nex.core.model.vo.UserVO;
import com.lijs.nex.role.model.pager.RolePage;
import com.lijs.nex.role.model.request.RoleRequest;
import com.lijs.nex.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/role")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@Slf4j
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    @Resource
    private UserApiClient userApiClient;

    /**
     * 根据ID查询角色
     */
    @GetMapping("/{id}")
    public BaseResponse<RoleVO> getRoleVO(@PathVariable("id") String id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(roleService.getRoleVOById(id));
    }

    @GetMapping("/page")
    public BaseResponse<IPage<RoleVO>> getRolePage(@RequestBody RolePage page, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(roleService.getRolePage(page));
    }

    @PostMapping("/add")
    public BaseResponse<RoleVO> add(@RequestBody RoleRequest roleRequest, HttpServletRequest request) {
        // 校验参数是否为空
        if (roleRequest == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(roleService.add(roleRequest));
    }

    @PutMapping("/update")
    public BaseResponse<RoleVO> update(@RequestBody RoleRequest roleRequest, HttpServletRequest request) {
        // 校验参数是否为空
        if (roleRequest == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(roleService.update(roleRequest));
    }

    @GetMapping("/user")
    public BaseResponse<List<UserVO>> listUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        UserVO userVO = new UserVO();
        userVO.setName(username);
        return ResultUtils.success(null);
    }

    @GetMapping("/search")
    public BaseResponse<List<UserDO>> searchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like(UserDO::getName, username);
        }
        return ResultUtils.success(null);
    }

    @DeleteMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody List<String> ids, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.NO_AUTH);
        }
        if (ids == null || ids.size() == 0) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        if (ids.contains(CommonConstants.ADMINISTRATOR.DEFAULT_ID)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        boolean result = roleService.logicDeleteByIds(ids);
        return ResultUtils.success(result);
    }

}
