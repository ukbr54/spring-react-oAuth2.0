package com.fancyfrog.security;

import com.fancyfrog.config.props.AppProperties;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */

@Slf4j
@Service
public class TokenProvider {

    private AppProperties appProperties;

    public TokenProvider(AppProperties appProperties){
        this.appProperties = appProperties;
    }

    public String createToken(Authentication authentication){
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(Long.toString(principal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public Long getUserIdFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken){
        try{
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e){
            log.info("Invalid JWT Signature");
        } catch (MalformedJwtException e){
            log.info("Invalid JWT token");
        } catch (ExpiredJwtException e){
            log.info("Expired JWT token");
        } catch (UnsupportedJwtException e){
            log.info("Unsupported JWT token");
        } catch (IllegalArgumentException ex){
            log.info("JWT claims string is empty.");
        }
        return false;
    }
}
