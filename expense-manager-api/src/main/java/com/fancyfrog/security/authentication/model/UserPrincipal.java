package com.fancyfrog.security.authentication.model;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Getter
public class UserPrincipal {

    private final Long userId;
    private final String email;
    private final List<GrantedAuthority>authorities;
    private final String provider;

    public UserPrincipal(Long userId, String email, List<GrantedAuthority> authorities, String provider) {
        this.userId = userId;
        this.email = email;
        this.authorities = authorities;
        this.provider = provider;
    }

    public static UserPrincipal create(Long userId, String email, List<GrantedAuthority> authorities, String provider){
        if(StringUtils.isBlank(email)) throw new IllegalArgumentException("Username is blank: "+email);
        return new UserPrincipal(userId,email,authorities,provider);
    }
}
