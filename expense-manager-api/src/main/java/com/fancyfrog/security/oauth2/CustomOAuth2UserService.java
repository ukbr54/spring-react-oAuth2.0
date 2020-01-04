package com.fancyfrog.security.oauth2;

import com.fancyfrog.persistence.AuthProvider;
import com.fancyfrog.persistence.model.Role;
import com.fancyfrog.persistence.model.User;
import com.fancyfrog.persistence.model.UserRole;
import com.fancyfrog.persistence.repositories.UserRepository;
import com.fancyfrog.persistence.repositories.UserRoleRepository;
import com.fancyfrog.security.authentication.model.UserPrincipal;
import com.fancyfrog.security.common.exception.OAuth2AuthenticationProcessingException;
import com.fancyfrog.security.oauth2.external.OAuth2UserInfo;
import com.fancyfrog.security.oauth2.external.OAuth2UserInfoFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private @Autowired UserRepository userRepository;
    private @Autowired UserRoleRepository userRoleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try{
            OAuth2User user = processOAuth2User(userRequest, oAuth2User);
            return user;
        }catch (AuthenticationException ex){
            throw ex;
        }catch (Exception e){
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest,OAuth2User oAuthUser){
        OAuth2UserInfo userInfo =
                OAuth2UserInfoFactory.getOAuth2UserInfo(userRequest.getClientRegistration().getRegistrationId(), oAuthUser.getAttributes());

        if(StringUtils.isBlank(userInfo.getEmail())){
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        User user = userRepository.findByEmail(userInfo.getEmail())
                .flatMap(u -> getIfPresent(u, userInfo)).orElseGet(() -> registerNewUser(userRequest, userInfo));

        UserPrincipal principal = UserPrincipal.create(user, oAuthUser.getAttributes());
        return principal;
    }

    private Optional<User> getIfPresent(User user, OAuth2UserInfo oAuthUser){
        return Objects.isNull(user) ? Optional.empty() : Optional.of(updateExistingUser(user,oAuthUser));
    }

    private User registerNewUser(OAuth2UserRequest userRequest, OAuth2UserInfo userInfo){
        User user = new User();
        user.setProvider(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(userInfo.getId());
        user.setName(userInfo.getName());
        user.setEmail(userInfo.getEmail());
        user.setImageUrl(userInfo.getImageUrl());
        User newUser = userRepository.save(user);
        UserRole role = userRoleRepository.save(new UserRole(new UserRole.Id(newUser.getId(), Role.USER), Role.USER));
        newUser.setRoles(Arrays.asList(role));
        return newUser;
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuthUser) {
        existingUser.setName(oAuthUser.getName());
        existingUser.setImageUrl(oAuthUser.getImageUrl());
        return userRepository.save(existingUser);
    }
}
