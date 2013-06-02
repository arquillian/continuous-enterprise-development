package org.cedj.app.web.rest.core.test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.net.URL;

import org.cedj.app.web.rest.core.ResourceLink;
import org.cedj.app.web.rest.core.annotation.ResourceModel;
import org.cedj.app.web.rest.core.root.RootResource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;

@RunWith(Arquillian.class)
public class RootResourceTestCase {

    private static String BASE_XML_MEDIA_TYPE = "application/vnd.ced+xml";
    private static String ROOT_XML_MEDIA_TYPE = BASE_XML_MEDIA_TYPE + ";type=root";
    private static String BASE_JSON_MEDIA_TYPE = "application/vnd.ced+json";
    private static String ROOT_JSON_MEDIA_TYPE = BASE_JSON_MEDIA_TYPE + ";type=root";

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(false, ResourceLink.class.getPackage(), ResourceModel.class.getPackage())
                .addPackages(true, RootResource.class.getPackage())
                .addClasses(TestApplication.class, TestResource.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
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
    public void shouldAssambleRootResourcesXML() throws Exception {
        given().
        then().
            contentType(ROOT_XML_MEDIA_TYPE).
            body("root.link[0].@rel", equalTo("test")).
            body("root.link[0].@href", equalTo(new URL(baseURL, "api/test").toExternalForm())).
        when().
            get(baseURL + "api/");
    }

    @Test
    public void shouldAssambleRootResourcesJSON() throws Exception {
        given().
        then().
            contentType(ROOT_JSON_MEDIA_TYPE).
            body("link[0].rel", equalTo("test")).
            body("link[0].href", equalTo(new URL(baseURL, "api/test").toExternalForm())).
        when().
            get(baseURL + "api/");
    }
}
