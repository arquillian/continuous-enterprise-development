package org.cedj.geekseek.web.rest.conference.test.story;

import org.cedj.geekseek.web.rest.conference.test.ConferenceRestDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class CreateConferenceAndSessionStoryTestCase extends CreateConferenceAndSessionStory {

    // Given
    @Deployment(testable = false)
    public static WebArchive deploy() {
        return ConferenceRestDeployments.conference()
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // See super class for Story
}
