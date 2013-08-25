package org.cedj.geekseek.service.security.oauth;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.agorava.Twitter;
import org.agorava.core.api.oauth.OAuthAppSettings;
import org.agorava.core.oauth.SimpleOAuthAppSettingsBuilder;

@ApplicationScoped
@Startup @Singleton
public class SettingsProducer implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String PROP_API_KEY = "AUTH_API_KEY";
    private static final String PROP_API_SECRET = "AUTH_API_SECRET";
    private static final String PROP_API_CALLBACK = "AUTH_CALLBACK";

    @Produces @Twitter @ApplicationScoped
    public static OAuthAppSettings createSettings() {
        String apiKey = System.getenv(PROP_API_KEY);
        String apiSecret = System.getenv(PROP_API_SECRET);
        String apiCallback = System.getenv(PROP_API_CALLBACK);
        if(apiCallback == null) {
            apiCallback = "auth";
        }

        SimpleOAuthAppSettingsBuilder builder = new SimpleOAuthAppSettingsBuilder();
        builder.apiKey(apiKey).apiSecret(apiSecret).callback(apiCallback);

        return builder.build();
    }

    @PostConstruct
    public void validateEnvironment() {
        String apiKey = System.getenv(PROP_API_KEY);
        if(apiKey == null) {
            throw new IllegalStateException(PROP_API_KEY + " env variable must be set");
        }
        String apiSecret = System.getenv(PROP_API_SECRET);
        if(apiSecret == null) {
            throw new IllegalStateException(PROP_API_SECRET + " env variable must be set");
        }
    }
}
