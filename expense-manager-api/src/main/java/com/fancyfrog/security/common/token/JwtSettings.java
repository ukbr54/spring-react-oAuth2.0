package com.fancyfrog.security.common.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.security.jwt")
public class JwtSettings {

    private Integer tokenExpirationTime;
    private String tokenIssuer;
    private String tokenSigningKey;
    private Integer refreshTokenExpTime;
}
