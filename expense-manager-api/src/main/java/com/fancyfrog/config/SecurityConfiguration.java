package com.fancyfrog.config;

import com.fancyfrog.security.CustomUserDetailsService;
import com.fancyfrog.security.RestAuthenticationEntryPoint;
import com.fancyfrog.security.TokenAuthenticationFilter;
import com.fancyfrog.security.oauth2.CustomOAuth2UserService;
import com.fancyfrog.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.fancyfrog.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.fancyfrog.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private @Autowired CustomUserDetailsService userDetailsService;
    private @Autowired CustomOAuth2UserService oAuth2UserService;
    private @Autowired OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private @Autowired OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private @Autowired HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(){
        return new TokenAuthenticationFilter();
    }

    /**
     *  By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
     *  the authorization request. But, since our service is stateless, we can't save it in
     *  the session. We'll save the request in a Base64 encoded cookie instead.
    */

    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Add our custom Token based authentication filter
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http
          .cors()
          .and()
            .sessionManagement()
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and().csrf().ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
          .and().headers().frameOptions().sameOrigin()//allow use of frame to same origin urls
          .and()
            .csrf()
                .disable()
          .formLogin()
            .disable()
          .httpBasic()
            .disable()
          .exceptionHandling()
            .authenticationEntryPoint(new RestAuthenticationEntryPoint())
          .and()
            .authorizeRequests()
                .antMatchers("/h2-console/**","/", "/error", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js").permitAll()
                .antMatchers("/auth/**","/oauth2/**").permitAll()
                .anyRequest().authenticated()
          .and()
            .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository())
          .and()
            .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
          .and()
            .userInfoEndpoint()
               .userService(oAuth2UserService)
          .and()
            .successHandler(oAuth2AuthenticationSuccessHandler)
            .failureHandler(oAuth2AuthenticationFailureHandler);
    }
}
