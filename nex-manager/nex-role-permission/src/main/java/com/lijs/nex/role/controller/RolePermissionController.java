package com.lijs.nex.role.controller;

import cn.hutool.core.lang.tree.Tree;
import com.lijs.nex.common.base.enums.ErrorCodeEnum;
import com.lijs.nex.common.base.exception.BusinessException;
import com.lijs.nex.common.base.response.BaseResponse;
import com.lijs.nex.common.base.utils.ResultUtils;
import com.lijs.nex.common.web.controller.BaseController;
import com.lijs.nex.core.model.vo.PermissionVO;
import com.lijs.nex.role.service.RolePermissionService;
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
@RequestMapping("/role/permission")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@Slf4j
public class RolePermissionController extends BaseController {

    @Resource
    private RolePermissionService rolePermissionService;

    /**
     * 根据ID查询角色下的权限点
     */
    @GetMapping("/{id}")
    public BaseResponse<List<PermissionVO>> getRolePermissions(@PathVariable("id") String roleId, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(rolePermissionService.getRolePermissions(roleId));
    }

    /**
     * 获取当前角色可查询的所有菜单权限点
     */
    @GetMapping("/menu/tree")
    public BaseResponse<List<Tree<PermissionVO>>> getRolePermissionsMenuTree(HttpServletRequest request) {
        // 上下文获取角色ID
        // @PathVariable("roleId") String roleId,
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        String roleId = "";
        return ResultUtils.success(rolePermissionService.getRolePermissionsMenuTree(roleId));
    }

    @PutMapping
    public BaseResponse<Boolean> saveRolePermission(String roleId, String permissionIds, HttpServletRequest request) {
        // 校验参数是否为空
        if (StringUtils.isEmpty(roleId)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(rolePermissionService.saveRolePermission(roleId, permissionIds));
    }



}
