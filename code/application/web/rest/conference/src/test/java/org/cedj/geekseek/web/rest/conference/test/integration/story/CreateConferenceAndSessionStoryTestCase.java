package org.cedj.geekseek.web.rest.conference.test.integration.story;

import java.io.File;

import org.cedj.geekseek.web.rest.conference.test.integration.ConferenceRestDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class CreateConferenceAndSessionStoryTestCase extends CreateConferenceAndSessionStory {

    // Given
    @Deployment(testable = false)
    public static WebArchive deploy() {
        return ConferenceRestDeployments.conference()
                .addAsWebInfResource(new File("src/main/resources/META-INF/beans.xml"));
    }

    // See super class for Story
}
