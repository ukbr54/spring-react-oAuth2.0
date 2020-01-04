package com.fancyfrog.config;

import com.fancyfrog.security.common.RestAuthenticationEntryPoint;
import com.fancyfrog.security.common.token.extractor.TokenExtractor;
import com.fancyfrog.security.authentication.usernamePassword.jwt.JwtAuthenticationProvider;
import com.fancyfrog.security.authentication.usernamePassword.jwt.JwtTokenAuthenticationProcessingFilter;
import com.fancyfrog.security.authentication.usernamePassword.jwt.SkipPathRequestMatcher;
import com.fancyfrog.security.authentication.usernamePassword.UsernamePasswordAuthenticationProvider;
import com.fancyfrog.security.authentication.usernamePassword.UsernamePasswordProcessingFilter;
import com.fancyfrog.security.oauth2.CustomOAuth2UserService;
import com.fancyfrog.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.fancyfrog.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.fancyfrog.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public static final String AUTHENTICATION_URL = "/auth/login";
    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";

    private @Autowired CorsFilter corsFilter;
    private @Autowired ObjectMapper objectMapper;
    private @Autowired TokenExtractor tokenExtractor;
    private @Autowired @Qualifier("UsernamePasswordAuthenticationSuccessHandler") AuthenticationSuccessHandler successHandler;
    private @Autowired @Qualifier("UsernamePasswordAuthenticationFailureHandler") AuthenticationFailureHandler failureHandler;
    private @Autowired AuthenticationManager authenticationManager;
    private @Autowired CustomOAuth2UserService customOAuth2UserService;
    private @Autowired RestAuthenticationEntryPoint authenticationEntryPoint;
    private @Autowired JwtAuthenticationProvider jwtAuthenticationProvider;
    private @Autowired UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;

    private @Autowired @Qualifier("OAuth2AuthenticationSuccessHandler") OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private @Autowired @Qualifier("OAuth2AuthenticationFailureHandler") OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    protected UsernamePasswordProcessingFilter buildUsernamePasswordProcessingFilter(String loginEntryPoint) throws Exception{
        UsernamePasswordProcessingFilter processingFilter =
                new UsernamePasswordProcessingFilter(loginEntryPoint,successHandler,failureHandler,objectMapper);
        processingFilter.setAuthenticationManager(this.authenticationManager);
        return  processingFilter;
    }

    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(List<String> pathsToSkip, String pattern)
            throws Exception{
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip,pattern);
        JwtTokenAuthenticationProcessingFilter filter =
                new JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(usernamePasswordAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    /*
      By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
      the authorization request. But, since our service is stateless, we can't save it in
      the session. We'll save the request in a Base64 encoded cookie instead.
    */
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
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
          .csrf().ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
          .and()
             .headers().frameOptions().sameOrigin()//allow use of frame to same origin urls
          .and()
          .csrf().disable()// We don't need CSRF for JWT based authentication
          .exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint)
          .and()
             .csrf().disable()
             .formLogin().disable()
             .httpBasic().disable()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
             .authorizeRequests()
             .antMatchers(PUBLIC_API_URLS.toArray(new String[PUBLIC_API_URLS.size()])).permitAll()
          .and()
             .authorizeRequests()
             .antMatchers(API_ROOT_URL).authenticated()
          .and()
             .addFilterBefore(buildUsernamePasswordProcessingFilter(AUTHENTICATION_URL), UsernamePasswordAuthenticationFilter.class)
             .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(PUBLIC_API_URLS,API_ROOT_URL),UsernamePasswordAuthenticationFilter.class)
          .oauth2Login()
             .authorizationEndpoint()
             .baseUri("/oauth2/authorize")
             .authorizationRequestRepository(this.cookieAuthorizationRequestRepository())
          .and()
             .redirectionEndpoint()//after authentication with the external provider.
             .baseUri("/oauth2/callback/*")
          .and()
             .userInfoEndpoint()//Accessing User Information
             .userService(this.customOAuth2UserService)
          .and()
             .successHandler(oAuth2AuthenticationSuccessHandler)
             .failureHandler(oAuth2AuthenticationFailureHandler);
    }

    private List<String> PUBLIC_API_URLS = Arrays.asList(
            AUTHENTICATION_URL, "/h2-console/**","/oauth2/**"
    );
}
