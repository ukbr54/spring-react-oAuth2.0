package com.fancyfrog.security.authentication.common.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */
public class AuthMethodNotSupportedException extends AuthenticationServiceException {

    public AuthMethodNotSupportedException(String msg) {
        super(msg);
    }
}
