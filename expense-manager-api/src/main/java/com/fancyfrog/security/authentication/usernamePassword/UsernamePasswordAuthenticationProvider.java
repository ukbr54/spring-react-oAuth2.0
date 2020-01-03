package com.fancyfrog.security.authentication.usernamePassword;

import com.fancyfrog.persistence.model.User;
import com.fancyfrog.persistence.repositories.UserRepository;
import com.fancyfrog.security.authentication.model.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Slf4j
@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private @Autowired UserRepository userRepository;
    private @Autowired PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication,"No Authentication data provided");
        String username = (String)authentication.getPrincipal();
        String password = (String)authentication.getCredentials();

        log.debug("Fetching the detail from database: {}",username);
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found: " + username));

        if(!passwordEncoder.matches(password,user.getPassword())){
            log.error("Password Provided by user is not matched with stored password");
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid");
        }

        if(Objects.isNull(user.getRoles()) && CollectionUtils.isEmpty(user.getRoles())){
            log.error("User role is not present in the database");
            throw new InsufficientAuthenticationException("User has no role assigned");
        }

        List<GrantedAuthority> authorities = user.getRoles().stream().map(authority ->
                new SimpleGrantedAuthority(authority.getRole().authority())).collect(Collectors.toList());

        UserPrincipal userPrincipal = UserPrincipal.create(user.getId(), user.getEmail(), authorities, user.getProvider().name());

        log.debug("User Principal is successfully created and control is delegated to success Handler");
        return new UsernamePasswordAuthenticationToken(userPrincipal,null,userPrincipal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
