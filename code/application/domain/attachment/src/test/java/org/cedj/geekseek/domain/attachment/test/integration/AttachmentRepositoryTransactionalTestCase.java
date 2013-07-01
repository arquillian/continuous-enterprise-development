package org.cedj.geekseek.domain.attachment.test.integration;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import junit.framework.Assert;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.attachment.model.Attachment;
import org.cedj.geekseek.domain.test.integration.BaseTransactionalBehavior;
import org.cedj.geekseek.domain.test.integration.CoreDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AttachmentRepositoryTransactionalTestCase extends
    BaseTransactionalBehavior<Attachment, Repository<Attachment>> {

    private static final String UPDATED_TITLE = "TEST UPDATED";

    public AttachmentRepositoryTransactionalTestCase() {
        super(Attachment.class);
    }

    // Given
    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addAsLibraries(
                CoreDeployments.core(),
                AttachmentDeployments.attachmentWithCache())
            .addAsLibraries(AttachmentDeployments.resolveDependencies())
            .addClass(BaseTransactionalBehavior.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private Repository<Attachment> repository;

    @Override
    protected Attachment createNewDomainObject() {
        return createAttachment();
    }

    @Override
    protected void updateDomainObject(Attachment domain) {
        domain.setTitle(UPDATED_TITLE);
    }

    @Override
    protected void validateUpdatedDomainObject(Attachment domain) {
        Assert.assertEquals(UPDATED_TITLE, domain.getTitle());
    }

    @Override
    protected Repository<Attachment> getRepository() {
        return repository;
    }

    private Attachment createAttachment() {
        try {
            Attachment attachment  = new Attachment();
            attachment.setTitle("Test Attachment");
            attachment.setUrl(new URL("http://geekseek.org"));
            attachment.setMimeType("text/plain");
            return attachment;

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
