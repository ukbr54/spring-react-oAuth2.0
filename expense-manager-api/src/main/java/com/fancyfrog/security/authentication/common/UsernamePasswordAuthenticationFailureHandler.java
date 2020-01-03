package com.fancyfrog.security.authentication.common;

import com.fancyfrog.security.authentication.common.exception.AuthMethodNotSupportedException;
import com.fancyfrog.security.authentication.common.exception.JwtExpiredTokenException;
import com.fancyfrog.security.authentication.common.exception.response.ErrorCode;
import com.fancyfrog.security.authentication.common.exception.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Slf4j
@Component
public class UsernamePasswordAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private @Autowired ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
            throws IOException, ServletException {

        log.error("Authentication failed !!!");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if(ex instanceof BadCredentialsException){
            objectMapper.writeValue(response.getWriter(),
                    ErrorResponse.of("Invalid username or password", ErrorCode.AUTHENTICATION,HttpStatus.UNAUTHORIZED));
        }else if(ex instanceof JwtExpiredTokenException){
            objectMapper.writeValue(response.getWriter(),
                    ErrorResponse.of("Token has expired", ErrorCode.JWT_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED));
        }else if(ex instanceof AuthMethodNotSupportedException){
            objectMapper.writeValue(response.getWriter(),
                    ErrorResponse.of(ex.getMessage(), ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
        }else{
            objectMapper.writeValue(response.getWriter(),
                    ErrorResponse.of("Authentication failed", ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
        }
    }
}
