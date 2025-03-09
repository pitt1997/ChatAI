package com.lijs.fizz.common.spi.service.permission;

import java.util.List;

/**
 * @author ljs
 * @date 2024-10-07
 * @description 用户权限查询服务接口
 */
public interface DataPermissionService {

    /**
     * 查询指定用户的用户数据权限
     *
     * @param userId 用户ID
     * @return 用户数据权限集合
     */
    List<String> getUserIds(String userId);

    /**
     * 查询指定用户的组织机构数据权限
     *
     * @param userId 用户ID
     * @return 组织机构数据权限集合
     */
    List<String> getOrgIds(String userId);

    /**
     * 查询指定用户的资源数据权限
     *
     * @param userId 用户ID
     * @return 资源数据权限集合
     */
    List<String> getResIds(String userId);

    /**
     * 查询指定用户的资源组数据权限
     *
     * @param userId 用户ID
     * @return 资源组数据权限集合
     */
    List<String> getResGroupIds(String userId);

    /**
     * 查询用户是否有超级角色
     *
     * @param userId 用户ID
     * @return true:有超级角色 false:无超级角色
     */
    boolean isSuperAdmin(String userId);

}
