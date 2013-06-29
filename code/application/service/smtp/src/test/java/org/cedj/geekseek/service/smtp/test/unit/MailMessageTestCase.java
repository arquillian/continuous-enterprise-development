package org.cedj.geekseek.service.smtp.test.unit;

import org.cedj.geekseek.service.smtp.MailMessageBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Simple POJO test cases for the {@link MailMessageBuilder}
 *
 * @author ALR
 */
public class MailMessageTestCase {

    @Test
    public void propsSet() {
        final MailMessageBuilder.MailMessage message = new MailMessageBuilder().
                from("from").addTo("to").subject("subject").body("body").
                contentType("contentType").build();
        Assert.assertEquals("from incorrect", "from", message.getFrom());
        Assert.assertEquals("to incorrect", "to", message.getTo()[0]);
        Assert.assertEquals("subject incorrect", "subject", message.getSubject());
        Assert.assertEquals("body incorrect", "body", message.getBody());
        Assert.assertEquals("contentType incorrect", "contentType", message.getContentType());
    }

    @Test(expected = IllegalStateException.class)
    public void fromAddressRequired() {
        new MailMessageBuilder().addTo("to").subject("subject").body("body").contentType("contentType").build();
    }

    @Test(expected = IllegalStateException.class)
    public void toAddressRequired() {
        new MailMessageBuilder().from("from").subject("subject").body("body").contentType("contentType").build();
    }

    @Test(expected = IllegalStateException.class)
    public void subjectRequired() {
        new MailMessageBuilder().addTo("to").from("from").body("body").contentType("contentType").build();
    }

    @Test(expected = IllegalStateException.class)
    public void bodyRequired() {
        new MailMessageBuilder().from("from").addTo("to").subject("subject").contentType("contentType").build();
    }

    @Test(expected = IllegalStateException.class)
    public void contentTypeRequired() {
        new MailMessageBuilder().from("from").addTo("to").subject("subject").body("body").build();
    }

}
