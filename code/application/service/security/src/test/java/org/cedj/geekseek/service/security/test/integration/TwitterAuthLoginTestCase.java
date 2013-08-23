package org.cedj.geekseek.service.security.test.integration;

import java.net.URL;

import org.cedj.geekseek.domain.persistence.test.integration.PersistenceDeployments;
import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.domain.user.test.integration.UserDeployments;
import org.cedj.geekseek.service.security.oauth.AuthServlet;
import org.cedj.geekseek.service.security.picketlink.OAuthAuthenticator;
import org.cedj.geekseek.service.security.test.arquillian.TwitterLogin;
import org.cedj.geekseek.service.security.user.UserRegistration;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class TwitterAuthLoginTestCase {

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            //.addAsResource("auth.properties")
            .addPackages(false,
                AuthServlet.class.getPackage(),
                OAuthAuthenticator.class.getPackage(),
                UserRegistration.class.getPackage())
            .addAsLibraries(UserDeployments.user())
            .addAsLibraries(
                Maven.resolver()
                    .loadPomFromFile("pom.xml")
                    .resolve("org.agorava:agorava-twitter-cdi", "org.picketlink:picketlink-impl")
                    .withTransitivity()
                    .asFile())
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsResource(new StringAsset(
                    PersistenceDeployments.descriptor()
                        .getAllPersistenceUnit().get(0)
                        .clazz(User.class.getName())
                        .up().exportAsString()), "META-INF/persistence.xml");
    }

    @ArquillianResource
    private URL base;

    @Test
    public void shouldLogin() throws Exception {

        URL auth = new URL(base, "auth");
        String session = new TwitterLogin().login(auth.toExternalForm());
        Assert.assertNotNull(session);
    }
}
