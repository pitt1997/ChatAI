package com.lijs.fizz.auth.controller;

import com.lijs.fizz.auth.constant.AuthConstant;
import com.lijs.fizz.auth.dto.JwtResponse;
import com.lijs.fizz.auth.dto.LoginRequest;
import com.lijs.fizz.auth.service.AuthService;
import com.lijs.fizz.common.base.enums.ErrorCodeEnum;
import com.lijs.fizz.common.base.response.BaseResponse;
import com.lijs.fizz.common.base.session.SessionUser;
import com.lijs.fizz.common.base.tenant.TenantContextHolder;
import com.lijs.fizz.common.base.token.JwtTokenProvider;
import com.lijs.fizz.common.base.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author ljs
 * @date 2025-02-11
 * @description
 */
@RestController
//@RequestMapping("/auth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Resource
    private AuthService authService;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * 授权码模式：认证页面
     * @param modelAndView
     * @param error 表单登录失败处理回调的错误信息
     * @return ModelAndView
     */
    @GetMapping("/auth/login")
    public ModelAndView require(ModelAndView modelAndView, @RequestParam(required = false) String error) {
        modelAndView.setViewName("ftl/login");
        modelAndView.addObject("error", error);
        return modelAndView;
    }

    /**
     * 授权码模式：确认页面
     * @return {@link ModelAndView }
     */
    @GetMapping("/auth/confirm")
    public ModelAndView confirm(Principal principal, ModelAndView modelAndView,
                                @RequestParam("client_id") String clientId,
                                @RequestParam("scope") String scope,
                                @RequestParam("state") String state) {

        Set<String> authorizedScopes = new HashSet<>();
        modelAndView.addObject("clientId", clientId);
        modelAndView.addObject("state", state);
        modelAndView.addObject("scopeList", authorizedScopes);
        modelAndView.addObject("principalName", principal.getName());
        modelAndView.setViewName("ftl/confirm");
        return modelAndView;
    }


    @GetMapping("/test")
    public BaseResponse<String> test() {
        System.out.println(1);
        return ResultUtils.success("hello world");
    }

    @PostMapping("/loginP")
    public BaseResponse<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        // 1. 校验用户名和密码不能为空
        if (StringUtils.isEmpty(loginRequest.getUsername()) || StringUtils.isEmpty(loginRequest.getPassword())) {
            return ResultUtils.error(ErrorCodeEnum.PARAMS_ERROR);
        }

        // 2. 尝试认证用户
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        System.out.println(authentication);

        SessionUser sessionUser = new SessionUser();
        sessionUser.setUserId(loginRequest.getUsername());
        sessionUser.setUsername(loginRequest.getUsername());
        String token = jwtTokenProvider.generateToken(sessionUser);
        return ResultUtils.success(new JwtResponse(token));
    }

    @GetMapping(value = {AuthConstant.AUTH_URL, "/"}, name = "URL鉴权")
    public void auth(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        SessionUser user = authService.getCurrentUser();
        String tenant = TenantContextHolder.get();
        if (StringUtils.isEmpty(tenant)) {
            tenant = AuthService.getParamTenant(request.getHeader(AuthConstant.X_FORWARDED_URI_HEADER));
        }
        TenantContextHolder.set(tenant);
        if (!Objects.equals(user.getTenantId(), tenant)) {
            logger.info("用户 【{}】,【{}】 没有权限访问 【{}】", user.getUserId(), user.getTenantId(), tenant);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write("{\"code\":403, \"message\":\"无权限访问\"}");
        }
        String path = authService.getPath(request.getHeader(AuthConstant.X_FORWARDED_URI_HEADER));
        // 设置域头
        // 忽略地址
        // 权限白名单
        // 鉴权
        authService.auth(request, response, user);
    }
}