package org.cedj.geekseek.service.security.test.integration;

import javax.enterprise.context.RequestScoped;

import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.BaseAuthenticator;
import org.picketlink.idm.model.sample.User;

@RequestScoped
@PicketLink
public class ControllableAuthenticator extends BaseAuthenticator {

    private boolean wasCalled = false;
    private boolean shouldFailAuth = false;

    @Override
    public void authenticate() {
        wasCalled = true;
        if(shouldFailAuth) {
            setStatus(AuthenticationStatus.FAILURE);
        } else {
            setStatus(AuthenticationStatus.SUCCESS);
            setAccount(new User());
        }
    }

    public boolean wasCalled() {
        return wasCalled;
    }

    public void setShouldFailAuth(boolean fail) {
        this.shouldFailAuth = fail;
    }

}
