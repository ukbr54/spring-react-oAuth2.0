package com.fancyfrog.security.authentication.jwt;

import com.fancyfrog.config.SecurityConfiguration;
import com.fancyfrog.security.authentication.common.token.JwtAuthenticationToken;
import com.fancyfrog.security.authentication.common.token.RawAccessToken;
import com.fancyfrog.security.authentication.common.token.extractor.TokenExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Slf4j
public class JwtTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationFailureHandler failureHandler;
    private final TokenExtractor tokenExtractor;

    public JwtTokenAuthenticationProcessingFilter(AuthenticationFailureHandler failureHandler,
                                                  TokenExtractor tokenExtractor,
                                                  RequestMatcher matcher) {
        super(matcher);
        this.failureHandler = failureHandler;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        String tokenPayload = request.getHeader(SecurityConfiguration.AUTHENTICATION_HEADER_NAME);
        if(Objects.isNull(tokenPayload)){
            log.error("Token header is not present");
            throw new InsufficientAuthenticationException("Token Header is not present");
        }

        String token = tokenExtractor.extract(tokenPayload);
        RawAccessToken accessToken = new RawAccessToken(token);
        log.debug("Intercepting all the secured URL and extracting the token from the header !!!!");
        return this.getAuthenticationManager().authenticate(new JwtAuthenticationToken(accessToken));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request,response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request,response,failed);
    }
}
