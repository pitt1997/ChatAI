package com.lijs.fizz.role.controller;

import com.lijs.fizz.common.base.enums.ErrorCodeEnum;
import com.lijs.fizz.common.base.exception.BusinessException;
import com.lijs.fizz.common.base.response.BaseResponse;
import com.lijs.fizz.common.base.utils.ResultUtils;
import com.lijs.fizz.common.web.controller.BaseController;
import com.lijs.fizz.role.service.RoleUserService;
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
@RequestMapping("/role/user")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@Slf4j
public class RoleUserController extends BaseController {

    @Resource
    private RoleUserService roleUserService;

    /**
     * 角色分配给用户
     *
     * @param roleId
     * @param userIds
     * @param request
     * @return
     */
    @PostMapping("/assign")
    public BaseResponse<Boolean> assignRoleToUser(String roleId, List<String> userIds, HttpServletRequest request) {
        // 校验参数是否为空
        if (StringUtils.isEmpty(roleId)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(roleUserService.assignRoleToUser(roleId, userIds));
    }

    /**
     * 删除角色下的用户
     *
     * @param roleId
     * @param deleteUserIds
     * @param request
     * @return
     */
    @PutMapping("/assign")
    public BaseResponse<Boolean> assignRoleToUser(String roleId, List<String> addUserIds, List<String> deleteUserIds, HttpServletRequest request) {
        // 校验参数是否为空
        if (StringUtils.isEmpty(roleId)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(roleUserService.assignRoleToUser(roleId, addUserIds, deleteUserIds));
    }

}
