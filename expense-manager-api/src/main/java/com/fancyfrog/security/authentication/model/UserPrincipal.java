package com.fancyfrog.security.authentication.model;

import com.fancyfrog.persistence.model.User;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Getter
public class UserPrincipal implements OAuth2User {

    private final Long userId;
    private final String email;
    private final List<GrantedAuthority>authorities;
    private final String provider;
    private Map<String, Object> attributes;

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

    public static  UserPrincipal create(User user){
        List<GrantedAuthority> authorities = user.getRoles().stream().map(authority ->
                new SimpleGrantedAuthority(authority.getRole().authority())).collect(Collectors.toList());
        if(StringUtils.isBlank(user.getEmail())) throw new IllegalArgumentException("Username is blank: "+user.getEmail());
        return new UserPrincipal(user.getId(),user.getEmail(),authorities,user.getProvider().name());
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return this.email;
    }
}
