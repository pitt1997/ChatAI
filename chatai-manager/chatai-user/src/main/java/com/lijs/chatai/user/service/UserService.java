package com.lijs.chatai.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lijs.chatai.common.base.session.SessionUser;
import com.lijs.chatai.core.model.entity.idm.UserDO;
import com.lijs.chatai.core.model.vo.UserVO;
import com.lijs.chatai.user.model.request.UserAddRequest;
import com.lijs.chatai.user.model.request.UserUpdateRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author pitt1
 * @description 针对表【t_user(用户表)】的数据库操作Service
 * @createDate 2025-01-02 15:26:56
 */
public interface UserService extends IService<UserDO> {

    UserVO getUserVOById(String id);

    UserVO getUserVOByName(String name);

    UserVO add(UserAddRequest userAddRequest);

    UserVO update(UserUpdateRequest userUpdateRequest);

    String userRegister(String userAccount, String userPassword, String checkPassword, String mobile);

    UserDO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    Integer userLogout(HttpServletRequest request);

    UserDO getSafetyUser(UserDO originUser);

    UserVO getSafetyUserVO(UserDO originUser);

    SessionUser getSessionUser(UserDO originUser);

    List<UserVO> selectUserList(UserVO userVO);

    IPage<UserVO> getUserPage(Page<?> page);

    UserDO getCurrentUser(HttpServletRequest request);

    int updateUser(UserDO user, UserDO loginUser);

    boolean logicDeleteById(String id);

    boolean logicDeleteByIds(List<String> ids);

}
