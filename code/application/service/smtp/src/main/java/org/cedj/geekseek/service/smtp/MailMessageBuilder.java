package org.cedj.geekseek.service.smtp;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Builder to construct immutable {@link MailMessage} instances
 *
 * @author ALR
 */
public class MailMessageBuilder implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String[] EMPTY = new String[]{};
    private String from;
    private String subject;
    private String body;
    private String contentType;
    private final Collection<String> toAddresses = new HashSet<String>();

    /**
     * Immutable object model representing an outgoing mail message to be sent
     */
    public class MailMessage implements Serializable{
        private static final long serialVersionUID = 1L;

        final String from;
        final String[] to;
        final String subject;
        final String body;
        final String contentType;

        MailMessage(final String from, final String[] to, final String subject, final String body, final String contentType) {
            this.from = from;
            this.to = to;
            this.subject = subject;
            this.body = body;
            this.contentType = contentType;
        }

        public String getFrom() {
            return from;
        }

        public String[] getTo() {
            return to;
        }

        public String getSubject() {
            return subject;
        }

        public String getBody() {
            return body;
        }

        public String getContentType() {
            return contentType;
        }

        @Override
        public String toString() {
            return "MailMessage{" +
                    "from='" + from + '\'' +
                    ", to=" + Arrays.toString(to) +
                    ", subject='" + subject + '\'' +
                    ", body='" + body + '\'' +
                    ", contentType='" + contentType + '\'' +
                    '}';
        }
    }

    public MailMessage build() throws IllegalStateException {

        // Validate
        if (from == null || from.length() == 0) {
            throw new IllegalStateException("from address must be specified");
        }
        if (toAddresses.size() == 0) {
            throw new IllegalStateException("at least one to address must be specified");
        }
        if (subject == null || subject.length() == 0) {
            throw new IllegalStateException("subject must be specified");
        }
        if (body == null || body.length() == 0) {
            throw new IllegalStateException("body must be specified");
        }
        if (contentType == null || contentType.length() == 0) {
            throw new IllegalStateException("contentType must be specified");
        }

        // Construct immutable object and return
        return new MailMessage(from, toAddresses.toArray(EMPTY), subject, body, contentType);

    }


    public MailMessageBuilder from(final String from) throws IllegalArgumentException {
        if (from == null || from.length() == 0) {
            throw new IllegalArgumentException("from address must be specified");
        }
        this.from = from;
        return this;
    }

    public MailMessageBuilder subject(final String subject) throws IllegalArgumentException {
        if (subject == null || subject.length() == 0) {
            throw new IllegalArgumentException("subject must be specified");
        }
        this.subject = subject;
        return this;
    }

    public MailMessageBuilder body(final String body) throws IllegalArgumentException {
        if (body == null || body.length() == 0) {
            throw new IllegalArgumentException("body must be specified");
        }
        this.body = body;
        return this;
    }

    public MailMessageBuilder contentType(final String contentType) throws IllegalArgumentException {
        if (contentType == null || contentType.length() == 0) {
            throw new IllegalArgumentException("contentType must be specified");
        }
        this.contentType = contentType;
        return this;
    }

    public MailMessageBuilder addTo(final String to) {
        if (to == null || to.length() == 0) {
            throw new IllegalArgumentException("to address must be specified");
        }
        toAddresses.add(to);
        return this;
    }
}
