package org.cedj.geekseek.service.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cedj.geekseek.domain.Current;
import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.service.security.picketlink.UserAccount;
import org.picketlink.Identity;

@RequestScoped
public class CurrentUserProducer {

    @Inject
    private Identity identity;

    @Produces @Current
    public User getCurrentUser() {
        if(identity.isLoggedIn()) {
            return ((UserAccount)identity.getAccount()).getUser();
        }
        return null;
    }
}
