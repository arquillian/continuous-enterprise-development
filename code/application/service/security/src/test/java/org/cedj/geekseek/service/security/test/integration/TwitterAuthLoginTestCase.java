package org.cedj.geekseek.service.security.test.integration;

import java.net.URL;

import org.cedj.geekseek.service.security.oauth.AuthServlet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class) @Ignore
public class TwitterAuthLoginTestCase {

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addAsResource("auth.properties")
            .addPackage(AuthServlet.class.getPackage())
            .addAsLibraries(
                Maven.resolver()
                    .offline()
                    .loadPomFromFile("pom.xml")
                    .resolve("org.agorava:agorava-twitter-cdi")
                    .withTransitivity()
                    .asFile())
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    private URL base;

    @Test
    public void shouldLogin() throws Exception {
        System.out.println(base);
    }
}
