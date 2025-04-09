package com.lijs.chatai.auth.provider;

import com.lijs.chatai.auth.service.CustomUserDetailsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author ljs
 * @date 2025-02-14
 * @description
 */
@Component
public class PwdAuthenticationProvider implements AuthenticationProvider {
    protected final Log logger = LogFactory.getLog(getClass());

    private final CustomUserDetailsService userDetailsService;

    public PwdAuthenticationProvider(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = determineUsername(authentication);
        UserDetails sessionUser;
        try {
            sessionUser = userDetailsService.loadUserByUsername(username);
            userDetailsService.checkPassword(authentication);
        } catch (UsernameNotFoundException ex) {
            this.logger.debug("Failed to find user '" + username + "'");
            throw new BadCredentialsException(ex.getMessage());
        }
        return successAuthentication(sessionUser, authentication);
    }

    public Authentication successAuthentication(Object principal, Authentication authentication) {
        // 是否给功能授权
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("admin");
        grantedAuthorities.add(grantedAuthority);
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(principal,
                authentication.getCredentials(), grantedAuthorities);
        result.setDetails(authentication.getDetails());
        this.logger.debug("Authenticated user...");
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private String determineUsername(Authentication authentication) {
        return (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
    }

    public CustomUserDetailsService getUserDetailsService() {
        return userDetailsService;
    }
}
