package com.fancyfrog.security.oauth2;

import com.fancyfrog.exception.OAuth2AuthenticationProcessingException;
import com.fancyfrog.persistence.AuthProvider;
import com.fancyfrog.persistence.model.User;
import com.fancyfrog.persistence.repositories.UserRepository;
import com.fancyfrog.security.UserPrincipal;
import com.fancyfrog.security.oauth2.user.OAuth2UserInfo;
import com.fancyfrog.security.oauth2.user.OAuth2UserInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private @Autowired UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try{
            return processOAuth2User(userRequest, oAuth2User);
        }catch (AuthenticationException ex){
            throw ex;
        }catch (Exception e){
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }

    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User){
        OAuth2UserInfo userInfo =
                OAuth2UserInfoFactory.getOAuth2UserInfo(userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(userInfo.getEmail())){
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        User user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(() -> registerNewUser(userRequest, userInfo));
        updateExistingUser(user,userInfo);
        return UserPrincipal.create(user,oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest userRequest, OAuth2UserInfo userInfo){
        User user = new User();
        user.setProvider(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(userInfo.getId());
        user.setName(userInfo.getName());
        user.setEmail(userInfo.getEmail());
        user.setImageUrl(userInfo.getImageUrl());
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}
