package org.cedj.geekseek.service.smtp;

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
        Assert.assertEquals("from incorrect", "from", message.from);
        Assert.assertEquals("to incorrect", "to", message.to[0]);
        Assert.assertEquals("subject incorrect", "subject", message.subject);
        Assert.assertEquals("body incorrect", "body", message.body);
        Assert.assertEquals("contentType incorrect", "contentType", message.contentType);
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
