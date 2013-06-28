package org.cedj.geekseek.web.rest.attachment.test;

import org.cedj.geekseek.domain.attachment.test.AttachmentDeployments;
import org.cedj.geekseek.domain.test.CoreDeployments;
import org.cedj.geekseek.web.rest.attachment.AttachmentResource;
import org.cedj.geekseek.web.rest.attachment.model.AttachmentRepresentation;
import org.cedj.geekseek.web.rest.core.test.RestCoreDeployments;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class AttachmentRestDeployments {

    private AttachmentRestDeployments() {
    }

    public static WebArchive attachment() {
        return ShrinkWrap.create(WebArchive.class)
            .addAsLibraries(
                AttachmentDeployments.attachment(),
                RestCoreDeployments.root(),
                CoreDeployments.core())
            .addPackage(AttachmentResource.class.getPackage())
            .addPackage(AttachmentRepresentation.class.getPackage())
            .addClasses(TestApplication.class, TestRepository.class);
    }
}
