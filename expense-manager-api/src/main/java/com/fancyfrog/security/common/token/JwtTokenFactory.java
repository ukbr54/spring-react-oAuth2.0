package com.fancyfrog.security.common.token;

import com.fancyfrog.security.authentication.model.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Slf4j
@Component
public class JwtTokenFactory {

    protected @Autowired JwtSettings settings;

    public JwtToken createAccessJwtToken(UserPrincipal userPrincipal){
        if(StringUtils.isBlank(userPrincipal.getEmail())){
            log.error("Cannot create JWT token without email");
            throw new IllegalArgumentException("Cannot create JWT token without email");
        }

        if(CollectionUtils.isEmpty(userPrincipal.getAuthorities())){
            log.error("User doesn't have any priviliges");
            throw new IllegalArgumentException("User doesn't have any priviliges");
        }

        Claims claims = Jwts.claims().setSubject(userPrincipal.getEmail());
        claims.put("scopes",userPrincipal.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toList()));
        claims.put("userId",userPrincipal.getUserId());
        claims.put("provider",userPrincipal.getProvider());

        LocalDateTime now = LocalDateTime.now();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(now.plusMinutes(settings.getTokenExpirationTime()).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();
        log.debug("jWT token generated Successfully!!!");
        return new AccessJwtToken(token,claims);
    }
}
