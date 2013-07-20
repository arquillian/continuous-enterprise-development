package org.cedj.geekseek.service.security.oauth;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;

import org.agorava.Twitter;
import org.agorava.core.api.oauth.OAuthSession;
import org.agorava.core.cdi.Current;

public class SessionProducer implements Serializable {

    private static final long serialVersionUID = 1L;

    @SessionScoped
    @Produces
    @Twitter
    @Current
    public OAuthSession produceOauthSession(@Twitter @Default OAuthSession session) {
        return session;
    }
}
