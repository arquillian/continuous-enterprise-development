package org.cedj.geekseek.web.rest.user.test.integration.story;

import org.cedj.geekseek.web.rest.user.test.integration.UserRestDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class CreateUserStoryTestCase extends CreateUserStory {

    // Given
    @Deployment(testable = false)
    public static WebArchive deploy() {
        return UserRestDeployments.user()
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // See super class for Story
}
