package com.fancyfrog.security.common.token;

import com.fancyfrog.security.common.exception.JwtExpiredTokenException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Slf4j
public class RawAccessToken implements JwtToken{

    private final String token;

    public RawAccessToken(String token) {
        this.token = token;
    }

    public Jws<Claims> parseClaims(String signingKey){
        try{
            log.debug("Parsing the JWT token !!!");
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
        }catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex){
            log.error("Inavlid Jwt token",ex);
            throw new BadCredentialsException("Invalid JWT token: ",ex);
        }catch (ExpiredJwtException ex){
            log.error("JWT token is expired ",ex);
            throw new JwtExpiredTokenException(this,"JWT token expired",ex);
        }
    }

    @Override
    public String getToken() {
        return this.token;
    }
}
