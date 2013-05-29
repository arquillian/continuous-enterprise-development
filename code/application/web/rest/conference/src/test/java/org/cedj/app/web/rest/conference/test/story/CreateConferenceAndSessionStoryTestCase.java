package org.cedj.app.web.rest.conference.test.story;

import java.io.File;

import org.cedj.app.web.rest.conference.test.ConferenceRestDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CreateConferenceAndSessionStoryTestCase extends CreateConferenceAndSessionStory {

    // Given
    @Deployment(testable = false)
    public static WebArchive deploy() {
        return ConferenceRestDeployments.conference()
                .addAsWebInfResource(new File("src/test/resources/beans.xml"));
    }

    // See super class for Story
}
