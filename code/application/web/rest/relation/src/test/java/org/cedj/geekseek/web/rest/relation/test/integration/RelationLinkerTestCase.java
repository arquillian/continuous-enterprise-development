package org.cedj.geekseek.web.rest.relation.test.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.net.URL;

import javax.ws.rs.core.Response.Status;

import org.cedj.geekseek.domain.relation.test.integration.RelationDeployments;
import org.cedj.geekseek.domain.relation.test.model.SourceObject;
import org.cedj.geekseek.domain.relation.test.model.SourceRepository;
import org.cedj.geekseek.domain.relation.test.model.TargetObject;
import org.cedj.geekseek.domain.relation.test.model.TargetRepository;
import org.cedj.geekseek.domain.relation.test.model.TestRepository;
import org.cedj.geekseek.domain.test.integration.CoreDeployments;
import org.cedj.geekseek.web.rest.core.test.integration.RestCoreDeployments;
import org.cedj.geekseek.web.rest.relation.RelationResource;
import org.cedj.geekseek.web.rest.relation.test.model.TestApplication;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;

@WarpTest @RunAsClient
@RunWith(Arquillian.class)
public class RelationLinkerTestCase {

    private static final String BASE_XML_MEDIA_TYPE = "application/vnd.ced+xml";

    private static final String SOURCE_ID = "11";
    private static final String TARGET_ID = "1";

    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addPackage(RelationResource.class.getPackage())
            .addPackage(TestApplication.class.getPackage())
            .addClasses(
                ValueInjectionInspection.class,
                TargetObject.class,
                SourceObject.class,
                TestRepository.class,
                SourceRepository.class,
                TargetRepository.class)
            .addAsLibraries(CoreDeployments.core(), RestCoreDeployments.rootWithJSON())
            .addAsLibraries(RestCoreDeployments.resolveDependencies())
            .addAsLibraries(RelationDeployments.relation())
            .addAsWebInfResource(RestCoreDeployments.linkableBeansXml(), "beans.xml");
    }

    @ArquillianResource
    private URL baseURL;

    private SourceObject source;
    private TargetObject target;

    @BeforeClass
    public static void setup() {
        RestAssured.filters(
                ResponseLoggingFilter.responseLogger(),
                new RequestLoggingFilter());
    }

    @Before
    public void createTypes() {
        source = new SourceObject(SOURCE_ID);
        target = new TargetObject(TARGET_ID);
    }

    @Test
    public void shouldBeAbleToAddRelation() throws Exception {
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                given().
                then().
                    contentType(BASE_XML_MEDIA_TYPE).
                    statusCode(Status.OK.getStatusCode()).
                    root("source").
                        body("link.find {it.@rel == 'connections'}.size()", equalTo(1)).
                        body("link.find {it.@rel == 'notdeployed'}.size()", equalTo(0)).
                when().
                    get(baseURL + "api/source/{id}",
                        SOURCE_ID);
            }
        }).inspect(new ValueInjectionInspection(target, source));
    }
}
