package com.fancyfrog.config;

import com.fancyfrog.persistence.AuthProvider;
import com.fancyfrog.persistence.model.Role;
import com.fancyfrog.persistence.model.User;
import com.fancyfrog.persistence.model.UserRole;
import com.fancyfrog.persistence.repositories.UserRepository;
import com.fancyfrog.persistence.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */
@Configuration
public class ApplicationStartupComponent {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Bean
    CommandLineRunner init(){
        return (s) ->saveUser();
    }

    private void saveUser(){
        User user = new User("user","user@gmail.com",passwordEncoder.encode("user"), AuthProvider.local);
        final User savedUser = createIfUserNotFound(user);
        userRoleRepository.save(new UserRole(new UserRole.Id(savedUser.getId(), Role.USER), Role.USER));


        User admin = new User("admin","admin@gmail.com",passwordEncoder.encode("admin"),AuthProvider.local);
        final User userAdmin = createIfUserNotFound(admin);
        userRoleRepository.save(new UserRole(new UserRole.Id(userAdmin.getId(), Role.ADMIN), Role.ADMIN));

    }

    private User createIfUserNotFound(User user){
        return userRepository.findByEmail(user.getEmail()).orElseGet(() -> userRepository.save(user));
    }
}
