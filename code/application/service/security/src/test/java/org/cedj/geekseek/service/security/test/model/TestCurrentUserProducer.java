package org.cedj.geekseek.service.security.test.model;

import javax.enterprise.inject.Produces;

import org.cedj.geekseek.domain.Current;
import org.cedj.geekseek.domain.user.model.User;

public class TestCurrentUserProducer {

    @Produces @Current
    private static User current;

    public void setCurrent(User current) {
        TestCurrentUserProducer.current = current;
    }
}
