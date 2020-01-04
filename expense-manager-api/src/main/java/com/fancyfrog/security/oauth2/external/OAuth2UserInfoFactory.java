package com.fancyfrog.security.oauth2.external;

import com.fancyfrog.security.common.exception.OAuth2AuthenticationProcessingException;
import com.fancyfrog.persistence.AuthProvider;

import java.util.Map;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String,Object> attributes){

        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())){
            return new GoogleOAuth2UserInfo(attributes);
        } else if(registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())){
            return new FacebookOAuth2UserInfo(attributes);
        } else if(registrationId.equalsIgnoreCase(AuthProvider.github.toString())){
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}