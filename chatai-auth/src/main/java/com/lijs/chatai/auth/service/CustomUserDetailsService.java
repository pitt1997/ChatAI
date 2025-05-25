package com.lijs.chatai.auth.service;

import com.lijs.chatai.common.base.response.BaseResponse;
import com.lijs.chatai.common.base.session.SessionUser;
import com.lijs.chatai.core.api.client.UserApiClient;
import com.lijs.chatai.core.model.request.UserLoginRequest;
import com.lijs.chatai.core.model.vo.UserVO;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    @Lazy // 注意单机版加载顺序 可能遇到加载不到到情况
    private UserApiClient userApiClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BaseResponse<UserVO> userResponse = userApiClient.getUserByName(username);
        if (userResponse != null && userResponse.getData() != null) {
            UserVO user = userResponse.getData();
            SessionUser sessionUser = new SessionUser();
            sessionUser.setUserId(user.getId());
            sessionUser.setUsername(user.getName());
            sessionUser.setPassword(user.getPassword());
            sessionUser.setOrganizationId(user.getOrganizationId());
            sessionUser.setTenantId(user.getTenantId());
            // TODO 可根据需求添加权限 URL权限
            sessionUser.getAuthorities().addAll(Collections.emptyList());
            return sessionUser;
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
