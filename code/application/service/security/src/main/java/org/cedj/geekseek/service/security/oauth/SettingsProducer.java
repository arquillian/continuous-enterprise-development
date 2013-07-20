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

    private static final String PROP_API_KEY = "auth.apikey";
    private static final String PROP_API_SECRET = "auth.apisecret";
    private static final String PROP_API_CALLBACK = "auth.callback";

    @Produces @Twitter
    public static OAuthAppSettings createSettings() {
        String apiKey = System.getProperty(PROP_API_KEY);
        if(apiKey == null) {
            throw new IllegalStateException(PROP_API_KEY + " system property must be set");
        }
        String apiSecret = System.getProperty(PROP_API_SECRET);
        if(apiSecret == null) {
            throw new IllegalStateException(PROP_API_SECRET + " system property must be set");
        }
        String apiCallback = System.getProperty(PROP_API_CALLBACK, "auth");

        SimpleOAuthAppSettingsBuilder builder = new SimpleOAuthAppSettingsBuilder();
        builder.apiKey(apiKey).apiSecret(apiSecret).callback(apiCallback);

        return builder.build();
    }

    @PostConstruct
    public void validateEnvironment() {
        String apiKey = System.getProperty(PROP_API_KEY);
        if(apiKey == null) {
            throw new IllegalStateException(PROP_API_KEY + " system property must be set");
        }
        String apiSecret = System.getProperty(PROP_API_SECRET);
        if(apiSecret == null) {
            throw new IllegalStateException(PROP_API_SECRET + " system property must be set");
        }
    }
}
