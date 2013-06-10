package org.cedj.geekseek.service.smtp;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Consumer of messages to the SMTP queue, implemented as an EJB Message-Driven Bean
 *
 * @author ALR
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = SMTPMailServiceConstants.JNDI_BIND_NAME_SMTP_QUEUE)})
public class SMTPMessageConsumer implements MessageListener {

    /**
     * Delegate to do the work
     */
    @EJB
    private SMTPMailService mailService;

    /**
     * Receives the next message off the JMS Queue, unwraps it, and
     * dispatches to the SMTP Mail Service to be sent
     * @param message
     */
    @Override
    public void onMessage(final Message message) {

        // Casting and unwrapping
        final ObjectMessage objectMessage;
        try {
            objectMessage = ObjectMessage.class.cast(message);
        } catch (final ClassCastException cce) {
            throw new RuntimeException("Incorrect message type sent to object message consumer; got:"
                    + message.getClass().getSimpleName(), cce);
        }
        final MailMessageBuilder.MailMessage mailMessage;
        try {
            final Object obj = objectMessage.getObject();
            mailMessage = MailMessageBuilder.MailMessage.class.cast(obj);
        } catch (final JMSException jmse) {
            throw new RuntimeException("Could not unwrap JMS Message", jmse);
        } catch (final ClassCastException cce) {
            throw new RuntimeException("Expected message contents of type "
                    + MailMessageBuilder.MailMessage.class.getSimpleName(), cce);
        }

        // Send the mail
        mailService.sendMail(mailMessage);
    }
}
