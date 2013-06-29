package org.cedj.geekseek.web.rest.attachment.test.integration.story;

import org.cedj.geekseek.web.rest.attachment.test.integration.AttachmentRestDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class CreateAttachmentStoryTestCase extends CreateAttachmentStory {

    // Given
    @Deployment(testable = false)
    public static WebArchive deploy() {
        return AttachmentRestDeployments.attachment()
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // See super class for Story
}
