package com.lijs.fizz.auth.service;

import com.lijs.fizz.common.base.response.BaseResponse;
import com.lijs.fizz.core.api.client.UserApiClient;
import com.lijs.fizz.core.model.request.UserLoginRequest;
import com.lijs.fizz.core.model.vo.UserVO;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author ljs
 * @date 2025-02-11
 * @description
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserApiClient userApiClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BaseResponse<UserVO> userResponse = userApiClient.getUserByName(username);
        if (userResponse != null && userResponse.getData() != null) {
            UserVO user = userResponse.getData();
            return new org.springframework.security.core.userdetails.User(
                    user.getName(),
                    user.getPassword(),
                    Collections.emptyList() // TODO 可根据需求添加权限 URL权限
            );
        }
        throw new UsernameNotFoundException("用户名或密码错误");  // 这里抛出异常
    }

    public void checkPassword(Authentication authentication) {
        try {
            String credentials = (String) authentication.getCredentials();
            UserLoginRequest userLoginRequest = new UserLoginRequest();
            userLoginRequest.setUserAccount((String) authentication.getPrincipal());
            userLoginRequest.setUserPassword(credentials);
            userApiClient.checkLoginPwd(userLoginRequest);
        } catch (FeignException e) {
            throw new BadCredentialsException("用户名或密码错误");
        }
    }
}
