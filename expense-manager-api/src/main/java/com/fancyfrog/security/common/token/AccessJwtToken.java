package com.fancyfrog.security.common.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

public class AccessJwtToken implements JwtToken{

    private final String rawToken;
    private final @JsonIgnore Claims claims;

    public AccessJwtToken(String rawToken, Claims claims) {
        this.rawToken = rawToken;
        this.claims = claims;
    }

    @Override
    public String getToken() {
        return this.rawToken;
    }
}
