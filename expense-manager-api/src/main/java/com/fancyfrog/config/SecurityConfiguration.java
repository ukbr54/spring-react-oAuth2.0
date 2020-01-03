package com.fancyfrog.config;

import com.fancyfrog.security.authentication.common.RestAuthenticationEntryPoint;
import com.fancyfrog.security.authentication.usernamePassword.UsernamePasswordAuthenticationProvider;
import com.fancyfrog.security.authentication.usernamePassword.UsernamePasswordProcessingFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String API_ROOT_URL = "/api/**";
    public static final String AUTHENTICATION_URL = "/api/authenticate";
    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";

    private @Autowired CorsFilter corsFilter;
    private @Autowired ObjectMapper objectMapper;
    private @Autowired AuthenticationSuccessHandler successHandler;
    private @Autowired AuthenticationFailureHandler failureHandler;
    private @Autowired AuthenticationManager authenticationManager;
    private @Autowired RestAuthenticationEntryPoint authenticationEntryPoint;
    private @Autowired UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;

    protected UsernamePasswordProcessingFilter buildUsernamePasswordProcessingFilter(String loginEntryPoint) throws Exception{
        UsernamePasswordProcessingFilter processingFilter =
                new UsernamePasswordProcessingFilter(loginEntryPoint,successHandler,failureHandler,objectMapper);
        processingFilter.setAuthenticationManager(this.authenticationManager);
        return  processingFilter;
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(usernamePasswordAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS,"/**")
                .antMatchers("/error", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
          .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
          .csrf().disable()// We don't need CSRF for JWT based authentication
          .exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint)
          .and()
             .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
             .authorizeRequests()
             .antMatchers(PUBLIC_API_URLS.toArray(new String[PUBLIC_API_URLS.size()])).permitAll()
          .and()
             .authorizeRequests()
             .antMatchers(API_ROOT_URL).authenticated()
          .and()
             .addFilterBefore(buildUsernamePasswordProcessingFilter(AUTHENTICATION_URL), UsernamePasswordAuthenticationFilter.class);
    }

    private List<String> PUBLIC_API_URLS = Arrays.asList(
            AUTHENTICATION_URL, "/h2-console/**","/expense/api/hello"
    );
}
