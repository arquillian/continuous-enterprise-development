package org.cedj.geekseek.domain.attachment.test.unit;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Date;

import org.cedj.geekseek.domain.attachment.model.Attachment;
import org.cedj.geekseek.domain.test.unit.TimestampableSpecification;
import org.junit.Test;

public class AttachmentValidationTestCase extends TimestampableSpecification<Attachment> {

    @Override
    protected Attachment createInstance() throws Exception {
        return new Attachment("", "", new URL("http://geekseek.org"));
    }

    @Override
    protected void forceCreated(Attachment entity) throws Exception {
        // date set during object creation
    }

    @Override
    protected void forceUpdate(Attachment entity) throws Exception {
        Field update = Attachment.class.getDeclaredField("updated");
        update.setAccessible(true);
        update.set(entity, new Date());
    }

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
}
