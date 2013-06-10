package org.cedj.geekseek.service.smtp;

/**
 * Constants for the mail service
 */
public interface SMTPMailServiceConstants {

    /**
     * Name in JNDI to which the SMTP {@link javax.mail.Session} will be bound
     */
    String JNDI_BIND_NAME_MAIL_SESSION = "java:jboss/mail/GeekSeekSMTP";

    /**
     * Name in JNDI to which the SMTP Queue is bound
     */
    String JNDI_BIND_NAME_SMTP_QUEUE = "java:/jms/queue/GeekSeekSMTP";
}
