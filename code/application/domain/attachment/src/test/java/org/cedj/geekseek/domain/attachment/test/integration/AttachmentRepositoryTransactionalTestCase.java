package org.cedj.geekseek.domain.attachment.test.integration;

import static org.cedj.geekseek.domain.attachment.test.TestUtils.createAttachment;

import javax.inject.Inject;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.attachment.model.Attachment;
import org.cedj.geekseek.domain.attachment.test.TestUtils;
import org.cedj.geekseek.domain.test.integration.BaseTransactionalSpecification;
import org.cedj.geekseek.domain.test.integration.CoreDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AttachmentRepositoryTransactionalTestCase extends
    BaseTransactionalSpecification<Attachment, Repository<Attachment>> {

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
            .addClasses(BaseTransactionalSpecification.class, TestUtils.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private Repository<Attachment> repository;

    @Override
    protected Attachment createNewDomainObject() {
        return createAttachment();
    }

    @Override
    protected Attachment updateDomainObject(Attachment domain) {
        return domain.setTitle(UPDATED_TITLE);
    }

    @Override
    protected void validateUpdatedDomainObject(Attachment domain) {
        Assert.assertEquals(UPDATED_TITLE, domain.getTitle());
    }

    @Override
    protected Repository<Attachment> getRepository() {
        return repository;
    }
}
