package org.cedj.geekseek.service.security.user;

import java.util.UUID;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.agorava.core.api.oauth.OAuthToken;
import org.agorava.twitter.model.TwitterProfile;
import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.service.security.oauth.SuccessfulAuthentication;

public class UserRegistration {

    @Inject
    private Repository<User> repository;

    //public void registerUser(@Observes SocialEvent<OAuthSession> event) { https://issues.jboss.org/browse/AGOVA-53
    public void registerUser(@Observes SuccessfulAuthentication event) {
        TwitterProfile profile = (TwitterProfile)event.getProfile();

        User user = repository.get(profile.getScreenName());
        if(user == null) {
            user = new User(profile.getScreenName());
        }
        user.setName(profile.getFullName());
        user.setBio(profile.getDescription());
        user.setAvatarUrl(profile.getProfileImageUrl());
        OAuthToken token = event.getToken();
        user.setAccessToken(token.getSecret() + "|" + token.getToken());
        if(user.getApiToken() == null) {
            user.setApiToken(UUID.randomUUID().toString());
        }

        repository.store(user);
    }
}
