package com.fancyfrog.security.authentication.common;

import com.fancyfrog.security.authentication.common.token.JwtToken;
import com.fancyfrog.security.authentication.common.token.JwtTokenFactory;
import com.fancyfrog.security.authentication.model.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Slf4j
@Component
public class UsernamePasswordAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private @Autowired JwtTokenFactory tokenFactory;
    private @Autowired ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        log.debug("Creating the JWT token for user: {}",principal.getEmail());
        JwtToken accessToken = tokenFactory.createAccessJwtToken(principal);

        //TODO: REFRESH TOKEN IMPLEMENTATION

        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken",accessToken.getToken());
        log.debug("Sending the JWT token in response");
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(httpServletResponse .getWriter(),tokenMap);
        clearAuthenticationAttributes(request);
    }

    /**
     * Removes temporary authentication-related data which may have been stored
     * in the session during the authentication process..
     *
     */
    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return;
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
