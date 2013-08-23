package org.cedj.geekseek.service.security.oauth;

import java.io.Serializable;

import org.agorava.core.api.UserProfile;
import org.agorava.core.api.oauth.OAuthToken;

public class SuccessfulAuthentication implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserProfile profile;
    private OAuthToken token;

    public SuccessfulAuthentication(UserProfile profile, OAuthToken token) {
        this.profile = profile;
        this.token = token;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public OAuthToken getToken() {
        return token;
    }
}
