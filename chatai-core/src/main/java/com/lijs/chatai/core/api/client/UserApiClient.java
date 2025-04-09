package com.lijs.chatai.core.api.client;

import com.lijs.chatai.common.base.response.BaseResponse;
import com.lijs.chatai.core.model.request.UserLoginRequest;
import com.lijs.chatai.core.model.vo.UserVO;
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
