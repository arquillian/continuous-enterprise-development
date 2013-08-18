package org.cedj.geekseek.service.security.test.integration;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import org.cedj.geekseek.service.security.oauth.AuthServlet;
import org.cedj.geekseek.service.security.picketlink.HttpObjectHolder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.servlet.AfterServlet;
import org.jboss.arquillian.warp.servlet.BeforeServlet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picketlink.annotations.PicketLink;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class AuthServletTestCase {

    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(AuthServlet.class, HttpObjectHolder.class, ControllableAuthenticator.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsLibraries(
                Maven.resolver()
                    .offline()
                    .loadPomFromFile("pom.xml")
                    .resolve("org.picketlink:picketlink-impl")
                        .withTransitivity()
                        .asFile());
    }

    @ArquillianResource
    private URL baseURL;

    @Test
    public void shouldRedirectToRefererOnAuthSuccess() throws Exception {
        Warp.initiate(new Activity() {

            @Override
            public void perform() {
                try {
                    final HttpURLConnection conn = (HttpURLConnection)new URL(baseURL, "auth").openConnection();
                    conn.setRequestProperty("Referer", "http:/geekseek.com");
                    conn.setInstanceFollowRedirects(false);
                    Assert.assertEquals(302, conn.getResponseCode());
                    Assert.assertEquals(conn.getHeaderField("Location"), "http:/geekseek.com");
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @Inject @PicketLink
            private ControllableAuthenticator auth;

            @BeforeServlet
            public void setup() {
                auth.setShouldFailAuth(false);
            }

            @AfterServlet
            public void validate() {
                Assert.assertTrue(auth.wasCalled());
            }
        });
    }

    @Test
    public void shouldReturnUnAuthorizedOnAuthFailure() throws Exception {
        Warp.initiate(new Activity() {

            @Override
            public void perform() {
                try {
                    final HttpURLConnection conn = (HttpURLConnection)new URL(baseURL, "auth").openConnection();
                    conn.setInstanceFollowRedirects(false);
                    Assert.assertEquals(400, conn.getResponseCode());
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).inspect(new Inspection() {
            private static final long serialVersionUID = 1L;

            @Inject @PicketLink
            private ControllableAuthenticator auth;

            @BeforeServlet
            public void setup() {
                auth.setShouldFailAuth(true);
            }

            @AfterServlet
            public void validate() {
                Assert.assertTrue(auth.wasCalled());
            }
        });
    }
}
