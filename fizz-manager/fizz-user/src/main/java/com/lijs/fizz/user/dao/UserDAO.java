package com.lijs.fizz.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lijs.fizz.core.model.entity.idm.UserDO;
import com.lijs.fizz.core.model.dto.DataPermission;
import com.lijs.fizz.core.model.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author pitt1
 * @description 针对表【t_user(用户表)】的数据库操作Mapper
 * @createDate 2025-01-02 15:26:56
 * @Entity com.lijs.fizz.core.entity.idm.UserDO
 */
public interface UserDAO extends BaseMapper<UserDO> {

    UserVO getUserVOById(@Param("id") String id, @Param("dataPermission") DataPermission dataPermission);

    UserVO getUserVOByName(@Param("name") String name, @Param("dataPermission") DataPermission dataPermission);

    List<UserVO> selectUserList(@Param("query") UserVO UserVO, @Param("dataPermission") DataPermission dataPermission);

    IPage<UserVO> getUserPage(@Param("page") Page<?> page, @Param("dataPermission") DataPermission dataPermission);

    int logicDeleteById(@Param("id") String id);

    int logicDeleteByIds(@Param("ids") List<String> ids);

}
