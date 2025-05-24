package com.lijs.chatai.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lijs.chatai.common.base.constant.CommonConstants;
import com.lijs.chatai.common.base.enums.ErrorCodeEnum;
import com.lijs.chatai.common.base.enums.StatusEnum;
import com.lijs.chatai.common.base.exception.BusinessException;
import com.lijs.chatai.common.base.session.SessionUser;
import com.lijs.chatai.common.base.utils.CryptoUtils;
import com.lijs.chatai.common.base.utils.RegexUtils;
import com.lijs.chatai.common.web.service.BaseService;
import com.lijs.chatai.core.model.dto.DataPermission;
import com.lijs.chatai.core.model.entity.idm.UserDO;
import com.lijs.chatai.core.model.vo.UserVO;
import com.lijs.chatai.user.dao.UserDAO;
import com.lijs.chatai.user.model.request.UserAddRequest;
import com.lijs.chatai.user.model.request.UserUpdateRequest;
import com.lijs.chatai.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author pitt1
 * @description 针对表【t_user(用户表)】的数据库操作Service实现
 * @createDate 2025-01-02 15:26:56
 */
@Service
public class UserServiceImpl extends BaseService<UserDAO, UserDO>
        implements UserService {

    @Resource
    private UserDAO userDAO;

    @Override
    public UserVO getUserVOById(String id) {
        DataPermission dataPermission = new DataPermission();
        return userDAO.getUserVOById(id, dataPermission);
    }

    @Override
    public UserVO getUserVOByName(String name) {
        DataPermission dataPermission = new DataPermission();
        return userDAO.getUserVOByName(name, dataPermission);
    }

    @Override
    public UserVO add(UserAddRequest userAddRequest) {
        // 校验
        checkUserAdd(userAddRequest);
        UserDO userDO = new UserDO();
        userDO.setName(userAddRequest.getName());
        userDO.setNickname(userAddRequest.getNickname());
        userDO.setCnName(userAddRequest.getCnName());
        userDO.setPassword(CryptoUtils.encrypt(userAddRequest.getPassword()));
        userDO.setOrganizationId(userAddRequest.getOrganizationId());
        userDO.setMobile(userAddRequest.getMobile());
        userDO.setEmail(userAddRequest.getEmail());
        userDO.setGender(userAddRequest.getGender());
        userDO.setDescription(userAddRequest.getDescription());
        userDO.setStatus(StatusEnum.NORMAL.getCode());
        // userDO.setIcon(new byte[]());
        save(userDO);
        return getSafetyUserVO(userDO);
    }

    @Override
    public UserVO update(UserUpdateRequest userUpdateRequest) {
        checkUserUpdate(userUpdateRequest);
        UserDO userDO = new UserDO();
        userDO.setId(userUpdateRequest.getId());
        userDO.setName(userUpdateRequest.getName());
        userDO.setNickname(userUpdateRequest.getNickname());
        userDO.setCnName(userUpdateRequest.getCnName());
        userDO.setPassword(CryptoUtils.encrypt(userUpdateRequest.getPassword()));
        userDO.setOrganizationId(userUpdateRequest.getOrganizationId());
        userDO.setMobile(userUpdateRequest.getMobile());
        userDO.setEmail(userUpdateRequest.getEmail());
        userDO.setGender(0);
        userDO.setDescription(userUpdateRequest.getDescription());
        userDO.setStatus(0);
        // userDO.setIcon(new byte[]());
        updateById(userDO);
        return getSafetyUserVO(userDO);
    }

    @Override
    public String userRegister(String userAccount, String userPassword, String checkPassword, String mobile) {
        // TODO 暂时注册到系统用户表中，后续使用注册中间表，审核后再放到系统用户表中
        UserAddRequest userAddRequest = new UserAddRequest();
        userAddRequest.setName(userAccount);
        userAddRequest.setPassword(userPassword);
        userAddRequest.setCheckPassword(checkPassword);
        userAddRequest.setMobile(mobile);
        checkUserAdd(userAddRequest);

        UserDO user = new UserDO();
        user.setName(userAccount);
        user.setPassword(CryptoUtils.encrypt(userPassword));
        user.setMobile(mobile);
        user.setStatus(StatusEnum.NORMAL.getCode());
        user.setIsDelete(StatusEnum.NORMAL.getCode());
        // user.setUserRole(0);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "保存用户失败");
        }
        return user.getId();
    }

    @Override
    public UserDO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }

        // 账户不能包含特殊字符
        if (findSpecialChar(userAccount)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "账户不能包含特殊字符");
        }
        // 查询用户是否存在
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", userAccount);
        UserDO user = userDAO.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            logger.info("user login failed, userAccount Cannot find");
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "用户名或密码错误");
        }

        if (!userPassword.equals(CryptoUtils.decrypt(user.getPassword()))) {
            logger.info("user login failed, userAccount Cannot match userPassword");
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "用户名或密码错误");
        }

        // 记录用户的登录状态
        request.getSession().setAttribute(CommonConstants.User.USER_LOGIN_STATE, getSessionUser(user));

        // 用户脱敏回显
        return getSafetyUser(user);
    }

    @Override
    public Integer userLogout(HttpServletRequest request) {
        // 注销用户登录信息
        request.getSession().removeAttribute(CommonConstants.User.USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public UserDO getSafetyUser(UserDO originUser) {
        if (originUser == null) {
            return null;
        }

        UserDO safetyUser = new UserDO();
        safetyUser.setId(originUser.getId());
        safetyUser.setName(originUser.getName());
        safetyUser.setCnName(originUser.getCnName());
        safetyUser.setIcon(originUser.getIcon());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setEmail(originUser.getEmail());
        //safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setMobile(originUser.getMobile());
        safetyUser.setStatus(originUser.getStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setCreateUser(originUser.getCreateUser());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        safetyUser.setUpdateUser(originUser.getUpdateUser());
        safetyUser.setOrganizationId(originUser.getOrganizationId());
        return safetyUser;
    }

    @Override
    public UserVO getSafetyUserVO(UserDO originUser) {
        if (originUser == null) {
            return null;
        }

        UserVO safetyUser = new UserVO();
        safetyUser.setId(originUser.getId());
        safetyUser.setName(originUser.getName());
        safetyUser.setCnName(originUser.getCnName());
        safetyUser.setIcon(originUser.getIcon());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setEmail(originUser.getEmail());
        // TODO 设置角色ID
        safetyUser.setUserRole(1);
        safetyUser.setMobile(originUser.getMobile());
        safetyUser.setStatus(originUser.getStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setCreateUser(originUser.getCreateUser());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        safetyUser.setUpdateUser(originUser.getUpdateUser());
        safetyUser.setOrganizationId(originUser.getOrganizationId());
        return safetyUser;
    }

    @Override
    public SessionUser getSessionUser(UserDO originUser) {
        if (originUser == null) {
            return null;
        }
        SessionUser sessionUser = new SessionUser();
        sessionUser.setUserId(originUser.getId());
        sessionUser.setUsername(originUser.getName());
        //sessionUser.setUserRole(originUser.getUserRole());
        sessionUser.setStatus(originUser.getStatus());
        sessionUser.setOrganizationId(originUser.getOrganizationId());
        return sessionUser;
    }

    @Override
    public List<UserVO> selectUserList(UserVO userVO) {
        DataPermission dataPermission = new DataPermission();
        return userDAO.selectUserList(userVO, dataPermission);
    }

    @Override
    public IPage<UserVO> getUserPage(Page<?> page) {
        DataPermission dataPermission = new DataPermission();
        return userDAO.getUserPage(page, dataPermission);
    }

    @Override
    public UserDO getCurrentUser(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }

        UserDO user = (UserDO) request.getSession().getAttribute(CommonConstants.User.USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_LOGIN);
        }

        return user;
    }

    @Override
    public int updateUser(UserDO user, UserDO loginUser) {
        String userId = user.getId();
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }

        // 2. 校验权限
        // 2.1 管理员可以更新任意信息
        // 2.2 用户只能更新自己的信息
        if (!isAdmin(loginUser) && !userId.equals(loginUser.getId())) {
            throw new BusinessException(ErrorCodeEnum.NO_AUTH);
        }

        UserDO oldUser = this.getById(user.getId());
        if (oldUser == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_NULL_ERROR);
        }

        // 3. 触发更新
        return this.baseMapper.updateById(user);
    }

    @Override
    public boolean logicDeleteById(String id) {
        return userDAO.logicDeleteById(id) > 0;
    }

    @Override
    public boolean logicDeleteByIds(List<String> ids) {
        return userDAO.logicDeleteByIds(ids) > 0;
    }

    /**
     * 是否为管理员
     *
     * @return
     */
    public boolean isAdmin(UserDO loginUser) {
//        UserDO user = (UserDO) request.getSession().getAttribute(CommonConstant.User.USER_LOGIN_STATE);
//        if (user == null || user.getUserRole() != CommonConstant.User.ADMIN_ROLE) {
//            return false;
//        }

        return true;
    }

    public void checkUserAdd(UserAddRequest userAddRequest) {
        String name = userAddRequest.getName();
        String password = userAddRequest.getPassword();
        String checkPassword = userAddRequest.getCheckPassword();
        String mobile = userAddRequest.getMobile();

        // 校验
        if (StringUtils.isAnyBlank(name, password, checkPassword)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "参数为空");
        }
        if (name.length() < 4) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "用户账号过短");
        }
        if (password.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "用户密码过短");
        }
        boolean mobileMatches = RegexUtils.MOBILE_TELEPHONE_PATTERN.matcher(mobile).matches();
        if (!mobileMatches) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "电话号码不准确");
        }

        // 账户不能包含特殊字符
        if (findSpecialChar(name)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "账号名不能包含特殊字符");
        }

        // 密码和校验密码相同
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "密码和校验密码不相同");
        }

        // 账户不能重复
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        long count = userDAO.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "账户不能重复");
        }
        // 手机号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        count = userDAO.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "手机号不能重复");
        }
    }

    private void checkUserUpdate(UserUpdateRequest userUpdateRequest) {
        String userId = userUpdateRequest.getId();
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        UserDO oldUser = this.getById(userId);
        if (oldUser == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_NULL_ERROR);
        }
        String name = userUpdateRequest.getName();
        String password = userUpdateRequest.getPassword();
        String checkPassword = userUpdateRequest.getCheckPassword();
        String mobile = userUpdateRequest.getMobile();
        String email = userUpdateRequest.getEmail();
        if (StringUtils.isNotEmpty(name)) {
            if (name.length() < 4) {
                throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "用户账号过短");
            }
            // 账户不能包含特殊字符
            if (findSpecialChar(name)) {
                throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "账号名不能包含特殊字符");
            }
            // 账户不能重复
            QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", name);
            long count = userDAO.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "账户不能重复");
            }
        }
        if (StringUtils.isNotEmpty(mobile)) {
            boolean mobileMatches = RegexUtils.MOBILE_TELEPHONE_PATTERN.matcher(mobile).matches();
            if (!mobileMatches) {
                throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "电话号码不准确");
            }
        }
        if (StringUtils.isNotEmpty(email)) {
            boolean mobileMatches = RegexUtils.EMAIL_PATTERN.matcher(email).matches();
            if (!mobileMatches) {
                throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "邮箱不准确");
            }
            // 手机号不能重复
            QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("mobile", mobile);
            long count = userDAO.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "手机号不能重复");
            }
        }
        if (StringUtils.isNotEmpty(password)) {
            if (password.length() < 8 || checkPassword.length() < 8) {
                throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "用户密码过短");
            }
            // 密码和校验密码相同
            if (!password.equals(checkPassword)) {
                throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "密码和校验密码不相同");
            }
        }
    }
}
