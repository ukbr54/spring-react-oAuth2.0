package com.fancyfrog.security.common.token.extractor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Slf4j
@Component
public class JwtHeaderTokenExtractor implements TokenExtractor{

    public static final String AUTHORIZATION_HEADER_PREFIX = "Bearer";

    @Override
    public String extract(String header) {
        if(StringUtils.isBlank(header)){
            log.error("Authorization Header Cannot be blank");
            throw new AuthenticationServiceException("Authorization Header cannot be blank");
        }

        if(header.length() < AUTHORIZATION_HEADER_PREFIX.length()){
            log.error("Invalid authorization header size");
            throw new AuthenticationServiceException("Invalid authorization header size");
        }

        log.debug("Accessing the token from the header and substring the prefix part!!");
        return header.substring(AUTHORIZATION_HEADER_PREFIX.length(),header.length());
    }
}
