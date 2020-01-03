package com.fancyfrog.security.authentication.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Configuration
public class CustomPasswordEncoder {

    @Bean
    protected BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
