/*
 * Licensed by the authors under the Creative Commons
 * Attribution-ShareAlike 2.0 Generic (CC BY-SA 2.0)
 * License:
 *
 * http://creativecommons.org/licenses/by-sa/2.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cedj.geekseek.service.smtp;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Sends outgoing mail messages to the {@link Session} bound in JNDI at
 * {@link SMTPMailServiceConstants#JNDI_BIND_NAME_MAIL_SESSION}.
 *
 * Supports existing transactions if one if in play, but does not require nor
 * create any if none is in flight.
 *
 * @author ALR
 */
@Singleton
@LocalBean
@TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
public class SMTPMailService {

    @Resource(lookup = SMTPMailServiceConstants.JNDI_BIND_NAME_MAIL_SESSION)
    private Session mailSession;

    @Resource(lookup = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = SMTPMailServiceConstants.JNDI_BIND_NAME_SMTP_QUEUE)
    private Queue smtpQueue;

    /**
     * Sends the message denoted by the required, specified value
     * object {@link MailMessageBuilder.MailMessage}
     *
     * @param mailMessage
     * @throws IllegalArgumentException If the message was not specified
     */
    public void sendMail(final MailMessageBuilder.MailMessage mailMessage) throws IllegalArgumentException {

        // Precondition check
        if (mailMessage == null) {
            throw new IllegalArgumentException("Mail message must be specified");
        }

        try {
            // Translate
            final MimeMessage mime = new MimeMessage(mailSession);
            final Address from = new InternetAddress(mailMessage.from);
            final int numToAddresses = mailMessage.to.length;
            final Address[] to = new InternetAddress[numToAddresses];
            for (int i = 0; i < numToAddresses; i++) {
                to[i] = new InternetAddress(mailMessage.to[i]);
            }
            mime.setFrom(from);
            mime.setRecipients(Message.RecipientType.TO, to);
            mime.setSubject(mailMessage.subject);
            mime.setContent(mailMessage.body, mailMessage.contentType);
            Transport.send(mime);
        } // Puke on error
        catch (final javax.mail.MessagingException e) {
            throw new RuntimeException("Error in sending " + mailMessage, e);
        }
    }

    /**
     * Queues the message denoted by the required, specified value
     * object {@link MailMessageBuilder.MailMessage} for sending (will
     * be processed by the queue without further action from the caller)
     *
     * @param mailMessage
     * @throws IllegalArgumentException If the message was not specified
     */
    public void queueMailForDelivery(final MailMessageBuilder.MailMessage mailMessage)
            throws IllegalArgumentException {

        // Precondition check
        if (mailMessage == null) {
            throw new IllegalArgumentException("Mail message must be specified");
        }

        try {
            final Connection connection = connectionFactory.createConnection();
            final javax.jms.Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            final MessageProducer producer = session.createProducer(smtpQueue);
            final ObjectMessage jmsMessage = session.createObjectMessage(mailMessage);
            producer.send(jmsMessage);
        } catch (final JMSException jmse) {
            throw new RuntimeException("Could not deliver mail message to the outgoing queue", jmse);
        }
    }
}
