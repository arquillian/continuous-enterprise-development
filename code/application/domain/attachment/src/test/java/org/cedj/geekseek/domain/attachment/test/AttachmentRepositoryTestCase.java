package org.cedj.geekseek.domain.attachment.test;

import java.net.URL;

import javax.inject.Inject;

import junit.framework.Assert;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.attachment.model.Attachment;
import org.cedj.geekseek.domain.test.CoreDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    }

    // Story: As a User I should be able to update an Attachment

    @Test
    public void shouldBeAbleToUpdateAttachmnt() throws Exception {
        Attachment attachment = createAttachment();
        attachment = repository.store(attachment);

        attachment.setTitle("Test 2");

        Attachment updated = repository.get(attachment.getId());

        Assert.assertEquals(attachment.getId(), updated.getId());
        Assert.assertEquals(attachment.getTitle(), updated.getTitle());
        Assert.assertEquals(attachment.getUrl(), updated.getUrl());
        Assert.assertEquals(attachment.getMimeType(), updated.getMimeType());
    }

    // Story: As a User I should be able to remove an Attachment

    @Test
    public void shouldBeAbleToRemoveAttachmnt() throws Exception {
        Attachment attachment = createAttachment();
        attachment = repository.store(attachment);

        repository.remove(attachment);

        Attachment removed = repository.get(attachment.getId());
        Assert.assertNull(removed);
    }

    private Attachment createAttachment() throws Exception {
        Attachment attachment  = new Attachment();
        attachment.setTitle("Test Attachment");
        attachment.setUrl(new URL("http://geekseek.org"));
        attachment.setMimeType("text/plain");
        return attachment;
    }
}