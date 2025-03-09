package com.lijs.nex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author ljs
 * @date 2025-03-05
 * @description
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用basic认证
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/login").permitAll() // 登录接口放行
                .antMatchers("/user/getUserByName").permitAll() // 公开接口放行
                .antMatchers("/user/search").permitAll() // 公开接口放行
                // .antMatchers("/api/public/**").permitAll() // 或者公共API
                .anyRequest().authenticated()
                .and();

        return http.build();
    }
}