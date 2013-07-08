package org.cedj.geekseek.web.rest.attachment.test.integration;

import static org.hamcrest.Matchers.equalTo;

import java.io.File;

import org.cedj.geekseek.domain.attachment.model.Attachment;
import org.cedj.geekseek.web.rest.attachment.test.model.AttachmentType;
import org.cedj.geekseek.web.rest.core.test.integration.resource.BaseRepositoryResourceSpecification;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

import com.jayway.restassured.specification.ResponseSpecification;

@WarpTest
@RunWith(Arquillian.class)
public class AttachmentResourceSpecificationTestCase extends BaseRepositoryResourceSpecification<Attachment, AttachmentType> {

    @Deployment
    public static WebArchive deploy() {
        return AttachmentRestDeployments.attachment()
                .addPackage(BaseRepositoryResourceSpecification.class.getPackage())
                .addAsWebInfResource(new File("src/test/resources/beans.xml"));
    }

    public AttachmentResourceSpecificationTestCase() {
        super(Attachment.class);
    }

    @Override
    protected String getBaseMediaType() {
        return AttachmentTypes.BASE_MEDIA_TYPE;
    }

    @Override
    protected String getTypedMediaType() {
        return AttachmentTypes.ATTACHMENT_MEDIA_TYPE;
    }

    @Override
    protected String getURISegment() {
        return "attachment";
    }

    @Override
    protected Attachment createDomainObject() {
        return new Attachment()
            .setTitle("Title")
            .setMimeType("application/test")
            .setUrl(getBaseURL());
    }

    @Override
    protected AttachmentType createUpdateRepresentation() {
        return new AttachmentType()
            .setTitle("Title")
            .setMimeType("application/test")
            .setUrl(getBaseURL());
    }

    @Override
    protected ResponseSpecification responseValidation(ResponseSpecification spec, Attachment attachment) {
        return spec.
            root("attachment").
                body("title", equalTo(attachment.getTitle())).
                body("mimeType", equalTo(attachment.getMimeType())).
                body("url", equalTo(attachment.getUrl().toExternalForm()));
    }
}