package com.biy.social.curvydolphin.service;

import com.biy.social.curvydolphin.entity.UserEntity;
import com.biy.social.curvydolphin.exceptions.UserException;
import com.biy.social.curvydolphin.model.User;
import com.biy.social.curvydolphin.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthorizationService {

    @Autowired
    private UserService userService;

    public User createOrUpdateAccount(String provider, OAuth2User oauthUser){

        switch (provider) {
            case "github":
                return createOrUpdateGithubAccount(oauthUser);
            case "google":
                return createOrUpdateGoogleAccount(oauthUser);
            default:
                throw new RuntimeException("Unknown provider");
        }

    }

    private User createOrUpdateGoogleAccount(OAuth2User oauthUser){
        String googleId =
                oauthUser.getAttribute("sub").toString();

        try {
            return userService.getByProviderId(googleId);
        } catch (UserException ex){
            // no user
            log.info("new User being created");
            User user = new User();
            user.setAvatar_url(oauthUser.getAttribute("picture"));
            user.setProviderId(googleId);
            user.setUsername(oauthUser.getAttribute("email"));
            user.setName(oauthUser.getAttribute("name"));
            return userService.createUser(user);
        }
    }

    private User createOrUpdateGithubAccount(OAuth2User oauthUser){
        String githubId =
                oauthUser.getAttribute("id").toString();

        try {
            return userService.getByProviderId(githubId);
        } catch (UserException ex){
            log.info("new User being created");
            User user = new User();
            user.setAvatar_url(oauthUser.getAttribute("avatar_url"));
            user.setProviderId(githubId);
            user.setUsername(oauthUser.getAttribute("login"));
            user.setName(oauthUser.getAttribute("name"));
            return userService.createUser(user);
        }
    }

    public User getCurrentAccount(){
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user");
        }

        OAuth2User oauthUser =
                (OAuth2User) authentication.getPrincipal();

        // convert to token to get provider smh
        OAuth2AuthenticationToken token =
                (OAuth2AuthenticationToken) authentication;
        // get token provider
        String provider = token.getAuthorizedClientRegistrationId();

        // if github else if google
        String providerId = null;

        switch (provider){
            case "github":
                providerId = oauthUser.getAttribute("id").toString();
                break;
            case "google":
                providerId = oauthUser.getAttribute("sub").toString();
                break;
            default:
                throw new RuntimeException("Provider not found");
        }

        return userService.getByProviderId(providerId);
    }
}
