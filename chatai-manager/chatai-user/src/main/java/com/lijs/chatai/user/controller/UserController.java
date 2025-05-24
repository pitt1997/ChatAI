package com.lijs.chatai.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lijs.chatai.common.base.constant.CommonConstants;
import com.lijs.chatai.common.base.enums.ErrorCodeEnum;
import com.lijs.chatai.common.base.exception.BusinessException;
import com.lijs.chatai.common.base.response.BaseResponse;
import com.lijs.chatai.common.base.session.SessionUser;
import com.lijs.chatai.common.base.session.SessionUserHelper;
import com.lijs.chatai.common.base.utils.ResultUtils;
import com.lijs.chatai.common.web.controller.BaseController;
import com.lijs.chatai.core.api.client.UserApiClient;
import com.lijs.chatai.core.model.entity.idm.UserDO;
import com.lijs.chatai.core.model.request.UserLoginRequest;
import com.lijs.chatai.core.model.vo.UserVO;
import com.lijs.chatai.user.model.pager.UserPage;
import com.lijs.chatai.user.model.request.UserAddRequest;
import com.lijs.chatai.user.model.request.UserRegisterRequest;
import com.lijs.chatai.user.model.request.UserUpdateRequest;
import com.lijs.chatai.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author author
 * @date 2025-01-04
 * @description
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
@Slf4j
public class UserController extends BaseController implements UserApiClient {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public BaseResponse<UserVO> getUserVO(@PathVariable("id") String id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.getUserVOById(id));
    }

    /**
     * 根据name查询用户
     */
    @GetMapping("/getUserByName")
    public BaseResponse<UserVO> getUserByName(String username) {
        if (!isAdmin(this.getCurrentHttpRequest())) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.getUserVOByName(username));
    }

    @GetMapping("/page")
    public BaseResponse<IPage<UserVO>> getUserPage(UserPage page, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.getUserPage(page));
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    public BaseResponse<UserVO> add(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        // 校验参数是否为空
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.add(userAddRequest));
    }

    /**
     * 修改用户
     */
    @PutMapping("/update")
    public BaseResponse<UserVO> update(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        // 校验参数是否为空
        if (userUpdateRequest == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.update(userUpdateRequest));
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/updateUser")
    public BaseResponse<Integer> updateUser(@RequestBody UserDO user, HttpServletRequest request) {
        // 校验参数是否为空
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }

        UserDO loginUser = userService.getCurrentUser(request);
        int result = userService.updateUser(user, loginUser);
        return ResultUtils.success(result);
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
        boolean result = userService.logicDeleteByIds(ids);
        return ResultUtils.success(result);
    }

    @DeleteMapping("/deleteUser")
    public BaseResponse<Boolean> deleteUser(@RequestBody String id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.NO_AUTH);
        }
        if (CommonConstants.ADMINISTRATOR.DEFAULT_ID.equals(id)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        boolean result = userService.logicDeleteById(id);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<UserVO> getCurrentUser() {
        SessionUser currentUser = SessionUserHelper.getUser();
        String userId = currentUser.getUserId();
        // TODO 校验用户是否合法
        UserDO user = userService.getById(userId);
        UserVO safeTyUser = userService.getSafetyUserVO(user);
        // TODO 设置用户角色
        safeTyUser.setUserRole(1);
        return ResultUtils.success(safeTyUser);
    }

    @PostMapping("/register")
    public BaseResponse<String> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return ResultUtils.error(ErrorCodeEnum.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String mobile = userRegisterRequest.getMobile();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, mobile)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "请求参数为空");
        }
        String userId = userService.userRegister(userAccount, userPassword, checkPassword, mobile);
        return ResultUtils.success(userId);
    }

    @PostMapping("/login")
    public BaseResponse<UserDO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCodeEnum.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCodeEnum.PARAMS_ERROR);
        }
        UserDO user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 检查用户登录密码
     */
    @PostMapping("/checkLoginPwd")
    public BaseResponse<UserVO> checkLoginPwd(@RequestBody UserLoginRequest userLoginRequest) {
        if (!isAdmin(this.getCurrentHttpRequest())) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.getUserVOByName(userLoginRequest.getUserAccount()));
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "请求参数为空");
        }

        Integer result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/list")
    public BaseResponse<List<UserVO>> listUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        UserVO userVO = new UserVO();
        userVO.setName(username);
        List<UserVO> userList = userService.selectUserList(userVO);
        return ResultUtils.success(userList);
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
        List<UserDO> userList = userService.list(queryWrapper);
        List<UserDO> resultList = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(resultList);
    }

    /**
     * 分页查询推荐用户
     */
    @GetMapping("/recommend")
    public BaseResponse<Page<UserDO>> recommendUsers(long pageSize, long pageNum, HttpServletRequest request) {
        UserDO loginUser = userService.getCurrentUser(request);
        String redisKey = String.format("chatai:user:recommend:%s", loginUser.getId());
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 判断缓存中是否存在，缓存存在直接读取缓存
        Page<UserDO> userPage = (Page<UserDO>) valueOperations.get(redisKey);
        if (userPage != null) {
            return ResultUtils.success(userPage);
        }

        // 缓存中不存在则查询数据库
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        userPage = userService.page(new Page<>(pageNum, pageSize), queryWrapper);

        return ResultUtils.success(userPage);
    }

}
