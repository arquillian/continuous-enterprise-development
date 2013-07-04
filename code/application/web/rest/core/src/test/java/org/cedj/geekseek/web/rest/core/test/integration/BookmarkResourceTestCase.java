package org.cedj.geekseek.web.rest.core.test.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.net.URL;

import javax.ws.rs.core.Response;

import org.cedj.geekseek.domain.test.integration.CoreDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;

@RunWith(Arquillian.class)
public class BookmarkResourceTestCase {

    public static final String TEST_MEDIA_TYPE = "application/vnd.ced+xml;type=test";

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addAsLibraries(CoreDeployments.core())
                .addAsLibraries(RestCoreDeployments.rootWithJSON())
                .addAsLibraries(RestCoreDeployments.resolveDependencies())
                .addClasses(TestApplication.class, TestResource.class, TestRepresentation.class, TestObject.class)
                .addAsWebInfResource(RestCoreDeployments.linkableBeansXml(), "beans.xml");
    }

    @ArquillianResource
    private URL baseURL;

    @BeforeClass
    public static void setup() {
        RestAssured.filters(
                ResponseLoggingFilter.responseLogger(),
                new RequestLoggingFilter());
    }

    @Test
    public void shouldRedirectToResource() throws Exception {
        given().
            redirects().
                follow(false).
        then().
            statusCode(Response.Status.SEE_OTHER.getStatusCode()).
            header("Location", new URL(baseURL, "api/test/200").toExternalForm()).
        when().
            get(baseURL + "api/bookmark/test/200");
    }

    @Test
    public void shouldReturnNotFoundOnUnknownType() throws Exception {
        given().
        then().
            statusCode(Response.Status.NOT_FOUND.getStatusCode()).
        when().
            get(baseURL + "api/bookmark/missing/200");
    }

    @Test
    public void shouldProvideBookmarkLinkToResource() throws Exception {
        given().
            contentType(TEST_MEDIA_TYPE).
        then().
            contentType(TEST_MEDIA_TYPE).
            statusCode(Response.Status.OK.getStatusCode()).
            body("test.link.find {it.@rel == 'bookmark'}.size()", equalTo(1)).
            body("test.link.@href", equalTo(new URL(baseURL, "api/bookmark/test/200").toExternalForm())).
        when().
            get(baseURL + "api/test/200");
    }
}
