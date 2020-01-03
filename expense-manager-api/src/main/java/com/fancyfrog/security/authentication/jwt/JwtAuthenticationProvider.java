package com.fancyfrog.security.authentication.jwt;

import com.fancyfrog.security.authentication.common.token.JwtAuthenticationToken;
import com.fancyfrog.security.authentication.common.token.JwtSettings;
import com.fancyfrog.security.authentication.common.token.RawAccessToken;
import com.fancyfrog.security.authentication.model.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private @Autowired JwtSettings settings;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessToken token = (RawAccessToken) authentication.getCredentials();
        Jws<Claims> claims = token.parseClaims(settings.getTokenSigningKey());
        String subject = claims.getBody().getSubject();
        List<String> scopes = (List<String>) claims.getBody().get("scopes");
        Integer userId = (Integer) claims.getBody().get("userId");
        String provider = (String) claims.getBody().get("provider");
        List<GrantedAuthority> authorities =
                scopes.stream().map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());

        UserPrincipal principal = UserPrincipal.create(Long.valueOf(userId.toString()), subject, authorities, provider);
        return new JwtAuthenticationToken(principal,authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
