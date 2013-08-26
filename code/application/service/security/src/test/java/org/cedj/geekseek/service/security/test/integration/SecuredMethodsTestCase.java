package org.cedj.geekseek.service.security.test.integration;

import static com.jayway.restassured.RestAssured.given;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.core.Response.Status;

import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.domain.user.test.integration.UserDeployments;
import org.cedj.geekseek.service.security.interceptor.SecurityInterceptor;
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
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunAsClient
@WarpTest
@RunWith(Arquillian.class)
public class SecuredMethodsTestCase {

    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(
                SecurityInterceptor.class,
                SecuredMethodsTestCase.class,
                SetupAuth.class,
                TestResource.class,
                TestApplication.class,
                TestCurrentUserProducer.class)
            .addAsLibraries(RestCoreDeployments.root())
            .addAsLibraries(UserDeployments.domain())
            .addAsWebInfResource(RestCoreDeployments.linkableBeansXml(), "beans.xml");
    }

    @ArquillianResource
    private URL baseURL;

    @Test
    public void shouldAllowOPTIONSForNonauthorizedAccess() throws Exception {
        final URL testURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                    then().
                        statusCode(Status.OK.getStatusCode()).
                when().
                    options(testURL.toExternalForm());
            }
        }).inspect(new SetupAuth(null));
    }

    @Test
    public void shouldAllowOPTIONSForAuthorizedAccess() throws Exception {
        final URL testURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                    then().
                        statusCode(Status.OK.getStatusCode()).
                when().
                    options(testURL.toExternalForm());
            }
        }).inspect(new SetupAuth(new User("testuser")));
    }

    @Test
    public void shouldAllowGETForUnauthorizedAccess() throws Exception {
        final URL testURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                    then().
                        statusCode(Status.OK.getStatusCode()).
                when().
                    get(testURL.toExternalForm());
            }
        }).inspect(new SetupAuth(null));
    }

    @Test
    public void shouldAllowGETForAuthorizedAccess() throws Exception {
        final URL testURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                    then().
                        statusCode(Status.OK.getStatusCode()).
                when().
                    get(testURL.toExternalForm());
            }
        }).inspect(new SetupAuth(new User("testuser")));
    }

    @Test
    public void shouldNotAllowPUTForUnauthorizedAccess() throws Exception {
        final URL testURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                    then().
                        statusCode(Status.UNAUTHORIZED.getStatusCode()).
                when().
                    put(testURL.toExternalForm());
            }
        }).inspect(new SetupAuth(null));
    }

    @Test
    public void shouldAllowPUTForAuuthorizedAccess() throws Exception {
        final URL testURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                    then().
                        statusCode(Status.OK.getStatusCode()).
                when().
                    put(testURL.toExternalForm());
            }
        }).inspect(new SetupAuth(new User("testuser")));
    }

    @Test
    public void shouldNotAllowPOSTForUnauthorizedAccess() throws Exception {
        final URL testURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                    then().
                        statusCode(Status.UNAUTHORIZED.getStatusCode()).
                when().
                    post(testURL.toExternalForm());
            }
        }).inspect(new SetupAuth(null));
    }

    @Test
    public void shouldAllowPOSTForAuthorizedAccess() throws Exception {
        final URL testURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                    then().
                        statusCode(Status.OK.getStatusCode()).
                when().
                    post(testURL.toExternalForm());
            }
        }).inspect(new SetupAuth(new User("testuser")));
    }

    @Test
    public void shouldNotAllowDELETEForUnauthorizedAccess() throws Exception {
        final URL testURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                    then().
                        statusCode(Status.UNAUTHORIZED.getStatusCode()).
                when().
                    delete(testURL.toExternalForm());
            }
        }).inspect(new SetupAuth(null));
    }

    @Test
    public void shouldAllowDELETEForAuthorizedAccess() throws Exception {
        final URL testURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                    then().
                        statusCode(Status.OK.getStatusCode()).
                when().
                    delete(testURL.toExternalForm());
            }
        }).inspect(new SetupAuth(new User("testuser")));
    }

    @Test
    public void shouldNotAllowPATCHForUnauthorizedAccess() throws Exception {
        final URL testURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                    then().
                        statusCode(Status.UNAUTHORIZED.getStatusCode()).
                when().
                    patch(testURL.toExternalForm());
            }
        }).inspect(new SetupAuth(null));
    }

    @Test
    public void shouldAllowPATCHForAuthorizedAccess() throws Exception {
        final URL testURL = createTestURL();
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                    given().
                    then().
                        statusCode(Status.OK.getStatusCode()).
                when().
                    patch(testURL.toExternalForm());
            }
        }).inspect(new SetupAuth(new User("testuser")));
    }

    private URL createTestURL() throws MalformedURLException {
        return new URL(baseURL, "api/test");
    }
}
