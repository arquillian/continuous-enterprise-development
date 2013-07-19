package org.cedj.geekseek.domain.attachment.test.unit;

import static org.cedj.geekseek.domain.attachment.test.TestUtils.createAttachment;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Date;

import org.cedj.geekseek.domain.attachment.model.Attachment;
import org.junit.Assert;
import org.junit.Test;

public class AttachmentValidationTestCase {

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullConstructorTitle() throws Exception {
        new Attachment(null, "", new URL("http://geekseek.org"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullConstructorMimeType() throws Exception {
        new Attachment("", null, new URL("http://geekseek.org"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullConstructorUrl() throws Exception {
        new Attachment("", "", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullSetterTitle() throws Exception {
        Attachment att = new Attachment("", "", new URL("http://geekseek.org"));
        att.setTitle(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullSetterMimeType() throws Exception {
        Attachment att = new Attachment("", "", new URL("http://geekseek.org"));
        att.setMimeType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullSetterUrl() throws Exception {
        Attachment att = new Attachment("", "", new URL("http://geekseek.org"));
        att.setUrl(null);
    }

    // Created/Updated/Modified leak test cases / Timestampable

    @Test
    public void shouldNotLeakCreatedDate() throws Exception {
        Attachment base = createAttachment();

        Date date = base.getCreated();
        date.setTime(100);

        Assert.assertNotEquals(date, base.getCreated());
    }

    @Test
    public void shouldNotLeakUpdatedDate() throws Exception {
        Attachment base = createAttachment();
        forceUpdate(base);
        Date date = base.getLastUpdated();
        date.setTime(100);

        Assert.assertNotEquals(date, base.getLastUpdated());
    }

    @Test
    public void shouldNotLeakModifiedDate() throws Exception {
        Attachment base = createAttachment();

        Date date = base.getLastModified();
        date.setTime(100);

        Assert.assertNotEquals(date, base.getLastModified());
    }

    @Test
    public void shouldUseUpdatedAsModifiedDateIfUpdated() throws Exception {
        Attachment base = createAttachment();
        Date created = base.getCreated();

        Date modified = base.getLastModified();
        Assert.assertEquals(created, modified);

        Thread.sleep(10); // force a tiny sleep to throw the dates off
        forceUpdate(base);

        modified = base.getLastModified();
        Assert.assertNotEquals(created, modified);
    }

    private void forceUpdate(Attachment att) throws Exception {
        Method update = Attachment.class.getDeclaredMethod("updated");
        update.setAccessible(true);
        update.invoke(att);
    }
}
