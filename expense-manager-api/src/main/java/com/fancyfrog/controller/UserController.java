package com.fancyfrog.controller;

import com.fancyfrog.exception.ResourceNotFoundException;
import com.fancyfrog.persistence.model.User;
import com.fancyfrog.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */

@RestController
public class UserController {

//    @Autowired
//    private UserRepository userRepository;
//
//    @GetMapping("/user/me")
//    @PreAuthorize("hasRole('USER')")
//    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
//        return userRepository.findById(userPrincipal.getId())
//                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
//    }
}
