package com.fancyfrog.security.authentication.usernamePassword;

import com.fancyfrog.security.common.exception.AuthMethodNotSupportedException;
import com.fancyfrog.security.authentication.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */

@Slf4j
public class UsernamePasswordProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private boolean postOnly = true;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final ObjectMapper objectMapper;

    public UsernamePasswordProcessingFilter(String filterProcessesUrl,
                                               AuthenticationSuccessHandler authenticationSuccessHandler,
                                               AuthenticationFailureHandler authenticationFailureHandler,
                                               ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(filterProcessesUrl));
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.objectMapper = objectMapper;
        log.debug("UsernamePasswordProcessingFilter Instantiation: {}", UsernamePasswordProcessingFilter.class);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        log.debug("De-serialization and basic validation of the incoming JSON payload ");
        if(!this.postOnly && !HttpMethod.POST.name().equals(request.getMethod())){
            log.error("Authentication method not supported. Request method: {}",request.getMethod());
            throw new AuthMethodNotSupportedException("Authentication method not supported");
        }

        LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);

        if(StringUtils.isBlank(loginRequest.getEmail()) || StringUtils.isBlank(loginRequest.getPassword())){
            log.error("Username or Password not provided");
            throw new AuthenticationServiceException("Username or Password not provided");
        }

        log.debug("Requested Authentication with email id: {}",loginRequest.getEmail());

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        authenticationSuccessHandler.onAuthenticationSuccess(request,response,authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
    }
}
