package org.cedj.geekseek.domain.attachment.test.integration;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.attachment.model.Attachment;
import org.cedj.geekseek.domain.attachment.test.TestUtils;
import org.cedj.geekseek.domain.test.integration.CoreDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.cedj.geekseek.domain.attachment.test.TestUtils.createAttachment;

@RunWith(Arquillian.class)
public class AttachmentRepositoryTestCase {

    // Given
    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addAsLibraries(
                CoreDeployments.core(),
                AttachmentDeployments.attachmentWithCache())
            .addAsLibraries(AttachmentDeployments.resolveDependencies())
            .addClass(TestUtils.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private Repository<Attachment> repository;

    // Story: As a User I should be able to create an Attachment

    @Test
    public void shouldBeAbleToCreateAttachment() throws Exception {
        Attachment attachment = createAttachment();
        repository.store(attachment);

        Attachment stored = repository.get(attachment.getId());
        Assert.assertNotNull(stored);

        Assert.assertEquals(attachment.getId(), stored.getId());
        Assert.assertEquals(attachment.getTitle(), stored.getTitle());
        Assert.assertEquals(attachment.getUrl(), stored.getUrl());
        Assert.assertEquals(attachment.getMimeType(), stored.getMimeType());
        Assert.assertNotNull(stored.getCreated());
    }

    // Story: As a User I should be able to update an Attachment

    @Test
    public void shouldBeAbleToUpdateAttachment() throws Exception {
        String updatedTitle = "Test 2";
        Attachment attachment = createAttachment();
        attachment = repository.store(attachment);

        attachment = attachment.setTitle(updatedTitle);
        attachment = repository.store(attachment);

        Attachment updated = repository.get(attachment.getId());

        Assert.assertEquals(updated.getTitle(), updatedTitle);
        Assert.assertNotNull(attachment.getLastUpdated());
    }

    // Story: As a User I should be able to remove an Attachment

    @Test
    public void shouldBeAbleToRemoveAttachment() throws Exception {
        Attachment attachment = createAttachment();
        attachment = repository.store(attachment);

        repository.remove(attachment);

        Attachment removed = repository.get(attachment.getId());
        Assert.assertNull(removed);
    }

    @Test
    public void shouldNotReflectNonStoredChanges() throws Exception {
        String updatedTitle = "Test 2";
        Attachment attachment = createAttachment();
        String originalTitle = attachment.getTitle();

        Attachment stored = repository.store(attachment);

        // tile change not stored to repository
        stored = stored.setTitle(updatedTitle);

        Attachment refreshed = repository.get(attachment.getId());

        Assert.assertEquals(refreshed.getTitle(), originalTitle);
    }
}