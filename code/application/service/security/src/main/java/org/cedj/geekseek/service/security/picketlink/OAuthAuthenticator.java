package org.cedj.geekseek.service.security.picketlink;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.agorava.Twitter;
import org.agorava.core.api.UserProfile;
import org.agorava.core.api.oauth.OAuthService;
import org.agorava.core.api.oauth.OAuthSession;
import org.agorava.core.cdi.Current;
import org.agorava.twitter.model.TwitterProfile;
import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.service.security.oauth.SuccessfulAuthentication;
import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.BaseAuthenticator;

@ApplicationScoped
@PicketLink
public class OAuthAuthenticator extends BaseAuthenticator {

    private static final String AUTH_COOKIE_NAME = "auth";
    private static final String LOCATION = "Location";

    @Inject @PicketLink
    private Instance<HttpServletRequest> requestInst;

    @Inject @PicketLink
    private Instance<HttpServletResponse> responseInst;

    @Inject
    private Repository<User> repository;

    @Inject
    private OAuthService service;

    @Inject @Twitter @Current
    private OAuthSession session;

    @Inject
    private Event<SuccessfulAuthentication> successful;

    @Override
    public void authenticate() {
        HttpServletRequest request = requestInst.get();
        HttpServletResponse response = responseInst.get();

        if(request == null || response == null) {
            setStatus(AuthenticationStatus.FAILURE);
        } else {
            if(session.isConnected()) { // already got a active session going
                OAuthSession session = service.getSession();
                UserProfile userProfile = session.getUserProfile();

                User user = repository.get(userProfile.getId());
                if(user == null) {  // can't find a matching account, shouldn't really happen
                    setStatus(AuthenticationStatus.FAILURE);
                } else {
                    setAccount(new UserAccount(user));
                    setStatus(AuthenticationStatus.SUCCESS);
                }
            } else {
                // Callback
                String verifier = request.getParameter(service.getVerifierParamName());
                if(verifier != null) {
                    session.setVerifier(verifier);
                    service.initAccessToken();

                    // https://issues.jboss.org/browse/AGOVA-53
                    successful.fire(new SuccessfulAuthentication(service.getSession().getUserProfile(), service.getAccessToken()));

                    String screenName = ((TwitterProfile)service.getSession().getUserProfile()).getScreenName();
                    User user = repository.get(screenName);
                    if(user == null) { // can't find a matching account
                        setStatus(AuthenticationStatus.FAILURE);
                    } else {
                        setAccount(new UserAccount(user));
                        setStatus(AuthenticationStatus.SUCCESS);
                        response.addCookie(new Cookie(AUTH_COOKIE_NAME, user.getApiToken()));
                    }

                } else {
                    // initiate redirect request to 3. party
                    String redirectUrl = service.getAuthorizationUrl();

                    response.setStatus(302);
                    response.setHeader(LOCATION, redirectUrl);
                    setStatus(AuthenticationStatus.DEFERRED);
                }
            }
        }
    }
}