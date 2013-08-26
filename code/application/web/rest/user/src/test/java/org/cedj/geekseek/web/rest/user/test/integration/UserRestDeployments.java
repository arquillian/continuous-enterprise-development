package org.cedj.geekseek.web.rest.user.test.integration;

import org.cedj.geekseek.domain.user.test.integration.UserDeployments;
import org.cedj.geekseek.web.rest.core.test.integration.RestCoreDeployments;
import org.cedj.geekseek.web.rest.user.UserResource;
import org.cedj.geekseek.web.rest.user.model.UserRepresentation;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class UserRestDeployments {

    private UserRestDeployments() {
    }

    public static JavaArchive module() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackage(UserResource.class.getPackage())
            .addPackage(UserRepresentation.class.getPackage());
    }

    public static WebArchive user() {
        return ShrinkWrap.create(WebArchive.class)
            .addAsLibraries(UserDeployments.domain(), RestCoreDeployments.root())
            .addPackage(UserResource.class.getPackage())
            .addPackage(UserRepresentation.class.getPackage())
            .addClasses(
                TestApplication.class,
                TestRepository.class);
    }
}
