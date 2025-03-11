package com.lijs.nex.auth.config;

import com.lijs.nex.auth.constant.AuthConstant;
import com.lijs.nex.auth.filter.JwtAuthenticationFilter;
import com.lijs.nex.auth.handler.CustomAuthFailureHandler;
import com.lijs.nex.auth.handler.CustomAuthSuccessHandler;
import com.lijs.nex.auth.provider.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author ljs
 * @date 2025-02-11
 * @description
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 启用方法级权限控制（如 @PreAuthorize）
public class SecurityConfig {

    @Autowired
    private CustomAuthSuccessHandler successHandler;
    @Autowired
    private CustomAuthFailureHandler failureHandler;

    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(PasswordEncoder passwordEncoder) {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        CustomAuthenticationProvider customAuthenticationProvider = customAuthenticationProvider(passwordEncoder());
        ProviderManager providerManager = new ProviderManager(customAuthenticationProvider);
        // providerManager.setAuthenticationEventPublisher(this.authenticationEventPublisher);
        return providerManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用httpBasic
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf().disable()
                // 禁用会话管理 每次请求携带认证信息（JWT）
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests() // security 5.7 版本可简写这里
                .antMatchers("/").permitAll() // 允许所有用户访问 `/`
                .antMatchers("/auth").permitAll() // 允许所有用户访问 `/auth`
                .antMatchers("/auth/login").permitAll() // 允许所有用户访问 `/auth/login`
                .antMatchers("/auth/chat").permitAll() // 允许所有用户访问 `/auth/login`
                .antMatchers("/api/ai/chat").permitAll() // 允许所有用户访问 `/auth/login`
                .antMatchers("/api/ai/chat/stream").permitAll() // 允许所有用户访问 `/auth/login`
                .antMatchers("/auth/confirm").permitAll() // 允许所有用户访问 `/auth/login`
                // .antMatchers("/public/**").permitAll() // 其他公开接口放行
                .anyRequest().authenticated()
                .and()
                .formLogin(formLogin -> formLogin
                        .usernameParameter(AuthConstant.USERNAME)
                        .passwordParameter(AuthConstant.PASSWORD)
                        .loginProcessingUrl(AuthConstant.LOGIN_URL)
                        .permitAll()
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                )
//                .logout(logout -> logout
//                        .logoutSuccessHandler(logoutSuccessHandler)
//                        .addLogoutHandler(logoutHandler)
//                )
                // 禁用缓存
                .requestCache(cache -> cache.requestCache(new NullRequestCache()))
                .authenticationProvider(customAuthenticationProvider(passwordEncoder())) // 添加自定义的 AuthenticationProvider
                //.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 默认 401 后续自定义
                //.exceptionHandling()
                // RESTFUL接口返回json方式
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            // 可能未注入
                            // 从 Session 读取错误信息
                            String errorMessage = (String) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
                            if (errorMessage == null) {
                                errorMessage = "认证失败，请重新登录";
                            }
                            // 获取当前应用的 contextPath (动态前缀，如 "/nex-auth")
                            String contextPath = request.getContextPath();  // 例如 "/nex-auth"
                            // 失败后重定向到登录页，并附带错误信息
                            response.sendRedirect(contextPath + "/auth/login?error=" + URLEncoder.encode(errorMessage, String.valueOf(StandardCharsets.UTF_8)));
                        })
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.setContentType("application/json;charset=UTF-8");
//                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                            response.getWriter().write("{\"code\":401, \"message\":\"未认证，请登录\"}");
//                        })
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            response.setContentType("application/json;charset=UTF-8");
//                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                            response.getWriter().write("{\"code\":403, \"message\":\"无权限访问\"}");
//                        })
                )
        // 传统重定向方式：未登录时重定向到 `/login
        //.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
        ;

        return http.build();
    }

}