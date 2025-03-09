package com.lijs.fizz.core.api.feign;

import com.lijs.fizz.common.base.constant.CommonConstants;
import com.lijs.fizz.common.base.response.BaseResponse;
import com.lijs.fizz.core.api.client.UserApiClient;
import com.lijs.fizz.core.model.request.UserLoginRequest;
import com.lijs.fizz.core.model.vo.UserVO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ljs
 * @date 2025-01-17
 * @description
 */
// 通过gateway跳转 TODO 后续实现自动发现版本的
@FeignClient(contextId = "userFeignClient", value = CommonConstants.Service.ADMIN_SERVICE, url = "http://172.16.136.10:8080")
//@FeignClient(contextId = "userFeignClient", value = CommonConstants.Service.GATEWAY_SERVICE)
@ConditionalOnProperty(name = "spring.cloud.nacos.discovery.enabled", havingValue = "true", matchIfMissing = true) // 微服务时加载
//@ConditionalOnProperty(name = "spring.cloud.nacos.discovery.enabled", havingValue = "false") // 单机版时加载
public interface UserFeignClient extends UserApiClient {

    @GetMapping("/admin/user/current")
    BaseResponse<UserVO> getCurrentUser();

    // @RequestLine(value = "GET /admin/user/getUserByName?username={username}")
    @GetMapping("/admin/user/getUserByName")
    // 使用 Spring MVC 注解
    BaseResponse<UserVO> getUserByName(@RequestParam("username") String username);

    @PostMapping("/admin/user/checkLoginPwd")
    BaseResponse<UserVO> checkLoginPwd(@RequestBody UserLoginRequest userLoginRequest);
}