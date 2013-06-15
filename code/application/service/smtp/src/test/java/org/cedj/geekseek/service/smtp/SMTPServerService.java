package org.cedj.geekseek.service.smtp;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import junit.framework.AssertionFailedError;

import org.subethamail.smtp.helper.SimpleMessageListener;
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter;
import org.subethamail.smtp.server.SMTPServer;

/**
 * Test fixture; installs an embedded SMTP Server on startup, shuts it down on undeployment.
 * Allows for pluggable handling of incoming messages for use in testing.
 *
 * @author ALR
 */
@Singleton
@Startup
@LocalBean
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class SMTPServerService {

    private static final Logger log = Logger.getLogger(SMTPServerService.class.getName());
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private static final int BIND_PORT = 25000;

    private SMTPServer server;
    private final PluggableReceiveHandlerMessageListener listener = new PluggableReceiveHandlerMessageListener();

    /**
     * Start the SMTP Embedded Server; called by the container during deployment
     *
     * @throws Exception
     */
    @PostConstruct
    public void startup() throws Exception {
        server = new SMTPServer(new SimpleMessageListenerAdapter(listener));
        server.setBindAddress(InetAddress.getLoopbackAddress());
        server.setPort(BIND_PORT);
        server.start();
    }

    /**
     * Stop the SMTP Server; called by the container on undeployment
     *
     * @throws Exception
     */
    @PreDestroy
    public void shutdown() throws Exception {
        server.stop();
    }

    /**
     * {@link SimpleMessageListener} implementation allowing extensible handling of
     * incoming SMTP events
     */
    private class PluggableReceiveHandlerMessageListener implements SimpleMessageListener {

        private TestReceiveHandler handler;

        @Override
        public boolean accept(String from, String recipient) {
            return true;
        }

        @Override
        public void deliver(final String from, final String recipient, final InputStream data)
            throws IOException {

            // Get contents as String
            byte[] buffer = new byte[4096];
            int read;
            final StringBuilder s = new StringBuilder();
            while ((read = data.read(buffer)) != -1) {
                s.append(new String(buffer, 0, read, CHARSET));
            }
            final String contents = s.toString();
            if (log.isLoggable(Level.INFO)) {
                log.info("Received SMTP event: " + contents);
            }

            // Pluggable handling
            if (handler == null) {
                log.warning("No SMTP receive handler has been associated");
            } else {
                handler.handle(contents);
            }
        }

        void setHandler(final TestReceiveHandler handler) {
            this.handler = handler;
        }

    }

    /**
     * General contract for actions to be taken on SMTP receive events; implement
     * and set {@link SMTPServerService#setHandler(org.cedj.geekseek.service.smtp.SMTPServerService.TestReceiveHandler)}
     * and pass an instance of this type in tests
     */
    interface TestReceiveHandler {
        void handle(String data) throws AssertionFailedError;
    }

    /**
     * Sets the handler for SMTP receive events; to be called as part of
     * test setup for test specific logic
     *
     * @param handler
     */
    public void setHandler(final TestReceiveHandler handler) {
        this.listener.setHandler(handler);
    }
}
