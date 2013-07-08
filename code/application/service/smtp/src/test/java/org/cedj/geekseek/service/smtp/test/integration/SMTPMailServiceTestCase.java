package org.cedj.geekseek.service.smtp.test.integration;

import java.io.File;
import java.net.InetAddress;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import junit.framework.AssertionFailedError;

import org.cedj.geekseek.service.smtp.MailMessageBuilder;
import org.cedj.geekseek.service.smtp.SMTPMailService;
import org.cedj.geekseek.service.smtp.SMTPMailServiceConstants;
import org.cedj.geekseek.service.smtp.SMTPMessageConsumer;
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.controller.client.MessageSeverity;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationMessageHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Ensures that our SMTP Service sends emails to an SMTP Server in both
 * synchronous and asynchronous calls.
 *
 * @author ALR
 */
@RunWith(Arquillian.class)
public class SMTPMailServiceTestCase {

    /**
     * Name of the deployment for manual operations
     */
    private static final String DEPLOYMENT_NAME = "mailService";

    /**
     * Deployment to be tested; will be manually deployed/undeployed
     * such that we can configure the server first
     *
     * @return
     */
    @Deployment(managed = false, name = DEPLOYMENT_NAME)
    public static WebArchive getApplicationDeployment() {
        final File[] subethamailandDeps = Maven.resolver().loadPomFromFile("pom.xml").resolve("org.subethamail:subethasmtp").withTransitivity().asFile();
        final WebArchive war = ShrinkWrap.create(WebArchive.class).addAsLibraries(subethamailandDeps).
                addClasses(SMTPMailService.class, MailMessageBuilder.class,
                        SMTPMailServiceConstants.class, SMTPMessageConsumer.class, SMTPServerService.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("META-INF/geekseek-smtp-queue-jms.xml");
        System.out.println(war.toString(true));
        return war;
    }

    /**
     * Service which sends email to a backing SMTP Server
     */
    @Inject
    private SMTPMailService mailService;

    /**
     * Hook into the embeddable SMTP server so we can customize its handling from the tests
     */
    @Inject
    private SMTPServerService smtpServerService;

    /**
     * Hook to Arquillian deployer so we can have fine-grained access as to when
     * our deployment is deployed/undeployed, in coordination with server config tasks
     */
    @ArquillianResource
    private Deployer deployer;

    /*
     * Lifecycle events; implemented as tests, though in truth they perform no assertions.  Used to configure
     * the server and deploy/undeploy the @Deployment archive at the appropriate times
     */

    @RunAsClient
    @InSequence(value = 1)
    @Test
    public void configureAppServer() throws Exception {

        /*
         * First configure a JavaMail Session for the Server to bind into JNDI; this
         * will be used by our MailService EJB.  In a production environment, we'll likely have configured
         * the server before it was started to point to a real SMTP server
         */

        final ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getLoopbackAddress(), 9999);

        final ModelNode createSocketBindingOperation = new ModelNode();
        createSocketBindingOperation.get("operation").set("add");
        createSocketBindingOperation.get("host").set("localhost");
        createSocketBindingOperation.get("port").set(25000);
        final ModelNode socketBindingAddress = createSocketBindingOperation.get("address");
        socketBindingAddress.add("socket-binding-group", "standard-sockets");
        socketBindingAddress.add("remote-destination-outbound-socket-binding", "mail-smtp-25000");
        System.out.println("Add remote outbound socket binding: " + client.execute(createSocketBindingOperation));

        final ModelNode createMailServiceOperation = new ModelNode();
        createMailServiceOperation.get("operation").set("add");
        createMailServiceOperation.get("jndi-name").set(SMTPMailServiceConstants.JNDI_BIND_NAME_MAIL_SESSION);
        createMailServiceOperation.get("debug").set("false");
        final ModelNode smtpAddress = createMailServiceOperation.get("address");
        smtpAddress.add("subsystem", "mail");
        smtpAddress.add("mail-session", SMTPMailServiceConstants.JNDI_BIND_NAME_MAIL_SESSION);
        System.out.println("Add mail service:" + client.execute(createMailServiceOperation));

        final ModelNode createSocketBindingRefOp = new ModelNode();
        createSocketBindingRefOp.get("operation").set("add");
        createSocketBindingRefOp.get("outbound-socket-binding-ref").set("mail-smtp-25000");
        final ModelNode socketBindingRefAddress = createSocketBindingRefOp.get("address");
        socketBindingRefAddress.add("subsystem", "mail");
        socketBindingRefAddress.add("mail-session", SMTPMailServiceConstants.JNDI_BIND_NAME_MAIL_SESSION);
        socketBindingRefAddress.add("server", "smtp");
        System.out.println("Configure mail service w/ socket binding:" + client.execute(createSocketBindingRefOp));

        final ModelNode reloadOperation = new ModelNode();
        reloadOperation.get("operation").set("reload");
        System.out.println("Reload config:" + client.execute(reloadOperation));

        client.close();

        /*
         * With the config all set and dependencies in place, now we can deploy
         */

        deployer.deploy(DEPLOYMENT_NAME);

    }

    @RunAsClient
    @InSequence(value = 3)
    @Test
    public void resetAppServerConfig()
            throws Exception {
        final ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getLoopbackAddress(), 9999);

        deployer.undeploy(DEPLOYMENT_NAME);

        final ModelNode removeSocketBindingOperation = new ModelNode();
        removeSocketBindingOperation.get("operation").set("remove");
        final ModelNode socketBindingAddress = removeSocketBindingOperation.get("address");
        socketBindingAddress.add("socket-binding-group", "standard-sockets");
        socketBindingAddress.add("remote-destination-outbound-socket-binding", "mail-smtp-25000");
        System.out.println("REMOVE SOCKETS" + client.execute(removeSocketBindingOperation));

        final ModelNode removeMailServiceOperation = new ModelNode();
        removeMailServiceOperation.get("operation").set("remove");
        final ModelNode smtpAddress = removeMailServiceOperation.get("address");
        smtpAddress.add("subsystem", "mail");
        smtpAddress.add("mail-session", SMTPMailServiceConstants.JNDI_BIND_NAME_MAIL_SESSION);
        System.out.println("REMOVE MAIL" + client.execute(removeMailServiceOperation));

        final ModelNode reloadOperation = new ModelNode();
        reloadOperation.get("operation").set("reload");
        System.out.println("Reload config:" + client.execute(reloadOperation));
        Thread.sleep(3000); // Because the operation returns but then server reload continues in the BG
                    // Find from the WildFly team a better notification mechanism upon which to wait
                    // https://github.com/arquillian/continuous-enterprise-development/issues/66

        client.close();
    }

    /*
     * TESTS
     */

    /**
     * Tests that mail can be sent asynchronously via a JMS Queue
     */
    @InSequence(value = 2)
    @Test
    public void testSmtpAsync() {

        // Set the body of the email to be sent
        final String body = "This is a test of the async SMTP Service";

        // Define a barrier for us to wait upon while email is sent through the JMS Queue
        final CyclicBarrier barrier = new CyclicBarrier(2);

        // Set a handler which will ensure the body was received properly
        smtpServerService.setHandler(new SMTPServerService.TestReceiveHandler() {
            @Override
            public void handle(final String contents) throws AssertionFailedError {
                try {

                    // Perform assertion
                    Assert.assertTrue("message received does not contain body sent in email", contents.contains(body));

                    // Should probably be the second and last to arrive, but this
                    // Thread can block indefinitely w/ no timeout needed.  If
                    // the test waiting on the barrier times out, it'll trigger a test
                    // failure and undeployment of the SMTP Service
                    barrier.await();
                } catch (final InterruptedException e) {
                    // Swallow, this would occur if undeployment were triggered
                    // because the test failed (and we'd get a proper
                    // AssertionFailureError on the client side)
                } catch (final BrokenBarrierException e) {
                    throw new RuntimeException("Broken test setup", e);
                }
            }
        });

        // Construct and send the message async
        final MailMessageBuilder.MailMessage message =
                new MailMessageBuilder().from("alr@continuousdev.org").addTo("alr@continuousdev.org")
                        .subject("Test").body(body).contentType("text/plain").build();
        mailService.queueMailForDelivery(message);

        // Wait on the barrier until the message is received by the SMTP
        // server (pass) or the test times out (failure)
        try {
            barrier.await(5, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            throw new RuntimeException("Broken test setup", e);
        } catch (final BrokenBarrierException e) {
            throw new RuntimeException("Broken test setup", e);
        } catch (final TimeoutException e) {
            // If the SMTP server hasn't processed the message in the allotted time
            Assert.fail("Test did not receive confirmation message in the allotted time");
        }
    }

}
