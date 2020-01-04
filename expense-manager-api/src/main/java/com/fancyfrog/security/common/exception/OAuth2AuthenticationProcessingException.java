package com.fancyfrog.security.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */
public class OAuth2AuthenticationProcessingException extends AuthenticationException {

    public OAuth2AuthenticationProcessingException(String msg, Throwable t) {
        super(msg, t);
    }

    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }
}
