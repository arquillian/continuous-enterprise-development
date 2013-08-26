package org.cedj.geekseek.service.security.test.model;

import org.cedj.geekseek.domain.user.model.User;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.servlet.BeforeServlet;

public class SetupAuth extends Inspection {

    private static final long serialVersionUID = 1L;

    private User user;

    public SetupAuth(User user) {
        this.user = user;
    }

    @BeforeServlet
    public void setup(TestCurrentUserProducer producer) {
        producer.setCurrent(this.user);
    }
}
