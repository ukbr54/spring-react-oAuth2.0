package com.fancyfrog.security.authentication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Setter
@Getter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
