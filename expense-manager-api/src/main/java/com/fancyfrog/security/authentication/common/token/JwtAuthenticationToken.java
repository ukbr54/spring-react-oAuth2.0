package com.fancyfrog.security.authentication.common.token;

import com.fancyfrog.security.authentication.model.UserPrincipal;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private RawAccessToken token;
    private UserPrincipal principal;

    public JwtAuthenticationToken(RawAccessToken token){
        super(null);
        this.token = token;
        super.setAuthenticated(false);
    }

    public JwtAuthenticationToken(UserPrincipal principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if(authenticated)
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.token = null;
    }
}
