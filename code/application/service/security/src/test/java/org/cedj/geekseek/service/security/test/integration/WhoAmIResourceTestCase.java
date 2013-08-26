package org.cedj.geekseek.service.security.test.integration;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.core.Response.Status;

import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.domain.user.test.integration.UserDeployments;
import org.cedj.geekseek.service.security.rest.WhoAmIResource;
import org.cedj.geekseek.service.security.test.model.SetupAuth;
import org.cedj.geekseek.service.security.test.model.TestApplication;
import org.cedj.geekseek.service.security.test.model.TestCurrentUserProducer;
import org.cedj.geekseek.web.rest.core.test.integration.RestCoreDeployments;
import org.cedj.geekseek.web.rest.user.test.integration.UserRestDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class WhoAmIResourceTestCase {

    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(
                WhoAmIResource.class,
                SetupAuth.class,
                TestApplication.class,
                TestCurrentUserProducer.class)
            .addAsLibraries(RestCoreDeployments.root())
            .addAsLibraries(UserDeployments.domain())
            .addAsLibraries(UserRestDeployments.module())
            .addAsWebInfResource(new File("src/test/resources/beans.xml"));
    }

    @ArquillianResource
    private URL baseURL;

    @Test
    public void shouldReponseWithNotAuthorizedWhenNoUserFound() throws Exception {
        final URL whoAmIURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                    then().
                        statusCode(Status.UNAUTHORIZED.getStatusCode()).
                when().
                    get(whoAmIURL.toExternalForm());
            }
        }).inspect(new SetupAuth(null));
    }

    @Test
    public void shouldReponseSeeOtherWhenUserFound() throws Exception {
        final URL whoAmIURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                        redirects().
                            follow(false).
                    then().
                        statusCode(Status.SEE_OTHER.getStatusCode()).
                when().
                    get(whoAmIURL.toExternalForm());
            }
        }).inspect(new SetupAuth(new User("testuser")));
    }

    private URL createTestURL() throws MalformedURLException {
        return new URL(baseURL, "api/security/whoami");
    }
}
