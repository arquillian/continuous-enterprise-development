package org.cedj.geekseek.service.security.user;

import java.util.UUID;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.agorava.core.api.event.SocialEvent;
import org.agorava.core.api.oauth.OAuthSession;
import org.agorava.core.api.oauth.OAuthToken;
import org.agorava.twitter.model.TwitterProfile;
import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.user.model.User;

public class UserRegistration {

    @Inject
    private Repository<User> repository;

    public void registerUser(@Observes SocialEvent<OAuthSession> event) {
        if(event.getStatus() == SocialEvent.Status.SUCCESS) {
            TwitterProfile profile = (TwitterProfile)event.getEventData().getUserProfile();

            User user = repository.get(profile.getScreenName());
            if(user == null) {
                user = new User(profile.getScreenName());
            }
            user.setName(profile.getFullName());
            user.setBio(profile.getDescription());
            user.setAvatarUrl(profile.getProfileImageUrl());
            OAuthToken token = event.getEventData().getAccessToken();
            user.setAccessToken(token.getSecret() + "|" + token.getToken());
            if(user.getApiToken() == null) {
                user.setApiToken(UUID.randomUUID().toString());
            }

            repository.store(user);
        }
    }
}
