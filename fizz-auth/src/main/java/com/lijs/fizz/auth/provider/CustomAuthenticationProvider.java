package com.lijs.fizz.auth.provider;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author ljs
 * @date 2025-02-14
 * @description
 */
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        String presentedPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new AuthenticationException("Invalid credentials") {
            };
        }
    }

    @Override
    protected UserDetails retrieveUser(String username,
                                       UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new AuthenticationException("User not found") {
            };
        }

        return userDetails;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Custom logic for authentication can go here
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // 加载用户信息
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        // 验证密码
        if (password.equals(userDetails.getPassword())) {
            // 密码匹配，返回认证成功的 Token
            return new UsernamePasswordAuthenticationToken(
                    userDetails,
                    password,
                    userDetails.getAuthorities()
            );
        } else {
            throw new BadCredentialsException("用户名或密码错误");
        }

//        if (passwordEncoder.matches(password, userDetails.getPassword())) {
//            // 密码匹配，返回认证成功的 Token
//            return new UsernamePasswordAuthenticationToken(
//                    userDetails,
//                    password,
//                    userDetails.getAuthorities()
//            );
//        } else {
//            throw new AccessDeniedException("用户名或密码错误");
//        }

        // return super.authenticate(authentication);
    }
}
