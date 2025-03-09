package com.lijs.fizz.core.api.client;

import com.lijs.fizz.common.base.response.BaseResponse;
import com.lijs.fizz.core.model.request.UserLoginRequest;
import com.lijs.fizz.core.model.vo.UserVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ljs
 * @date 2025-03-07
 * @description
 */
public interface UserApiClient {

    BaseResponse<UserVO> getCurrentUser();

    BaseResponse<UserVO> getUserByName(@RequestParam("username") String username);

    BaseResponse<UserVO> checkLoginPwd(@RequestBody UserLoginRequest userLoginRequest);

}
