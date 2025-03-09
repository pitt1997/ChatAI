package com.lijs.nex.core.api.feign;

import com.lijs.nex.common.base.constant.CommonConstants;
import com.lijs.nex.common.base.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author ljs
 * @date 2025-01-17
 * @description
 */
@FeignClient(contextId = "dataPermissionFeignClient", value = CommonConstants.Service.ADMIN_SERVICE)
public interface DataPermissionFeignClient {

    /**
     * 查询指定用户的用户数据权限
     */
    @GetMapping("/api/role/getUserIds")
    BaseResponse<List<String>> getUserIds(String userId);

    /**
     * 查询指定用户的用户数据权限
     */
    @GetMapping("/api/role/getOrgIds")
    BaseResponse<List<String>> getOrgIds(String userId);

    /**
     * 查询指定用户的资源组数据权限
     */
    @GetMapping("/api/role/getResGroupIds")
    BaseResponse<List<String>> getResGroupIds(String userId);

    /**
     * 查询用户是否有超级角色（admin账号）
     */
    @GetMapping("/api/role/isSuperAdmin")
    BaseResponse<Boolean> isSuperAdmin(String userId);

}