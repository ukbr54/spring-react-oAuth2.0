package com.fancyfrog.controller;

import com.fancyfrog.exception.BadRequestException;
import com.fancyfrog.payload.ApiResponse;
import com.fancyfrog.payload.AuthResponse;
import com.fancyfrog.payload.LoginRequest;
import com.fancyfrog.payload.SignUpRequest;
import com.fancyfrog.persistence.AuthProvider;
import com.fancyfrog.persistence.model.User;
import com.fancyfrog.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */

@RestController
@RequestMapping("/auth")
public class AuthController {

//    private @Autowired AuthenticationManager authenticationManager;
//    private @Autowired TokenProvider tokenProvider;
//    private @Autowired UserRepository userRepository;
//    private @Autowired PasswordEncoder passwordEncoder;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> authentication(@Valid @RequestBody LoginRequest loginRequest){
//        Authentication authenticate = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authenticate);
//        String token = tokenProvider.createToken(authenticate);
//        return ResponseEntity.ok(new AuthResponse(token));
//    }

//    @PostMapping("/signup")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest){
//        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
//            throw new BadRequestException("Email address already in use.");
//        }
//
//        // Creating user's account
//        User user = new User();
//        user.setName(signUpRequest.getName());
//        user.setEmail(signUpRequest.getEmail());
//        user.setPassword(signUpRequest.getPassword());
//        user.setProvider(AuthProvider.local);
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        User result = userRepository.save(user);
//
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentContextPath().path("/user/me")
//                .buildAndExpand(result.getId()).toUri();
//
//        return ResponseEntity.created(location)
//                .body(new ApiResponse(true, "User registered successfully@"));
//    }
}
