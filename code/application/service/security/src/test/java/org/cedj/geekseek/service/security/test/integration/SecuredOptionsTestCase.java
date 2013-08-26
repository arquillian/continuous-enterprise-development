package org.cedj.geekseek.service.security.test.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.core.Response.Status;

import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.domain.user.test.integration.UserDeployments;
import org.cedj.geekseek.service.security.resteasy.SecuredOptionsExceptionMapper;
import org.cedj.geekseek.service.security.test.model.SetupAuth;
import org.cedj.geekseek.service.security.test.model.TestApplication;
import org.cedj.geekseek.service.security.test.model.TestCurrentUserProducer;
import org.cedj.geekseek.service.security.test.model.TestResource;
import org.cedj.geekseek.web.rest.core.test.integration.RestCoreDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class SecuredOptionsTestCase {

    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(
                SecuredOptionsExceptionMapper.class,
                SecuredOptionsTestCase.class,
                SetupAuth.class,
                TestResource.class,
                TestApplication.class,
                TestCurrentUserProducer.class)
            .addAsLibraries(RestCoreDeployments.root())
            .addAsLibraries(UserDeployments.domain())
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    private URL baseURL;

    @Test
    public void shouldNotContainStateChangingMethodsForUnauthorizedAccess() throws Exception {
        final URL testURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                    then().
                        statusCode(Status.OK.getStatusCode()).
                        header("Allow", allOf(
                            not(containsString("POST")),
                            not(containsString("PUT")),
                            not(containsString("DELETE")),
                            not(containsString("PATCH")))).
                when().
                    options(testURL.toExternalForm());
            }
        }).inspect(new SetupAuth(null));
    }

    @Test
    public void shouldContainStateChangingMethodsForAuthorizedAccess() throws Exception {
        final URL testURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                    then().
                        statusCode(Status.OK.getStatusCode()).
                        header("Allow", allOf(
                            containsString("GET"),
                            containsString("OPTIONS"),
                            containsString("POST"),
                            containsString("PUT"),
                            containsString("DELETE"),
                            containsString("PATCH"))).
                when().
                    options(testURL.toExternalForm());
            }
        }).inspect(new SetupAuth(new User("testuser")));
    }

    private URL createTestURL() throws MalformedURLException {
        return new URL(baseURL, "api/test");
    }
}
