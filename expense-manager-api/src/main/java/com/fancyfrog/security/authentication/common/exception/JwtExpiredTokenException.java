package com.fancyfrog.security.authentication.common.exception;

import com.fancyfrog.security.authentication.common.token.JwtToken;
import org.springframework.security.core.AuthenticationException;


/**
 * Created by Ujjwal Gupta on Jan,2020
 */

public class JwtExpiredTokenException extends AuthenticationException {

    private JwtToken token;

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }

    public JwtExpiredTokenException(JwtToken token, String msg, Throwable t) {
        super(msg,t);
        this.token = token;
    }

    public String token() {
        return this.token.getToken();
    }
}
