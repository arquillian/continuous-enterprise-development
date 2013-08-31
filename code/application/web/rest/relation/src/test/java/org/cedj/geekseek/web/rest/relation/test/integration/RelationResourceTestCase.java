package org.cedj.geekseek.web.rest.relation.test.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.net.URI;
import java.net.URL;

import javax.ws.rs.core.Response.Status;

import org.cedj.geekseek.domain.relation.test.integration.RelationDeployments;
import org.cedj.geekseek.domain.relation.test.model.SourceObject;
import org.cedj.geekseek.domain.relation.test.model.SourceRepository;
import org.cedj.geekseek.domain.relation.test.model.TargetObject;
import org.cedj.geekseek.domain.relation.test.model.TargetRepository;
import org.cedj.geekseek.domain.relation.test.model.TestRepository;
import org.cedj.geekseek.domain.test.integration.CoreDeployments;
import org.cedj.geekseek.web.rest.core.ResourceLink;
import org.cedj.geekseek.web.rest.core.test.integration.RestCoreDeployments;
import org.cedj.geekseek.web.rest.relation.RelationResource;
import org.cedj.geekseek.web.rest.relation.test.model.TestApplication;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
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
public class RelationResourceTestCase {

    private static final String BASE_JSON_MEDIA_TYPE = "application/vnd.ced+json";

    private static final String SOURCE_ID = "10";
    private static final String TARGET_ID = "12";

    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addPackage(RelationResource.class.getPackage())
            //.addPackages(false, Filters.exclude(TestRelationRepository.class), TestApplication.class.getPackage())
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
            //.addAsLibraries(RelationDeployments.relationWithNeo())
            //.addAsLibraries(RelationDeployments.neo4j())
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    private URL baseURL;

    private SourceObject source;
    private TargetObject target;
    private String type;

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
        type = "SPEAKING";
    }

    @Test @InSequence(1)
    public void shouldBeAbleToAddRelation() throws Exception {
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                given().
                    contentType(BASE_JSON_MEDIA_TYPE).
                    body(new ResourceLink("test", URI.create("http://test.org/api/test/" + TARGET_ID + "/"))).
                then().
                    contentType(BASE_JSON_MEDIA_TYPE).
                    statusCode(Status.OK.getStatusCode()).
                when().
                    patch(baseURL + "api/rel/{sourceObj}/{source}/{rel}/{targetObj}/",
                        "sourceobject", SOURCE_ID, type, "targetobject");
            }
        }).inspect(new ValueInjectionInspection(target, source));
    }

    @Test @InSequence(2)
    public void shouldBeAbleToFindRelation() throws Exception {
        given().
        then().
            contentType(BASE_JSON_MEDIA_TYPE).
            statusCode(Status.OK.getStatusCode()).
            body("[0].id", equalTo(TARGET_ID)).
        when().
            get(baseURL + "api/rel/{sourceObj}/{source}/{rel}/{targetObj}", "sourceobject", SOURCE_ID, type, "targetobject");
    }

    @Test @InSequence(3)
    public void shouldBeAbleToRemoveRelation() throws Exception {
        given().
            contentType(BASE_JSON_MEDIA_TYPE).
            body(new ResourceLink("test", URI.create("http://test.org/api/test/" + TARGET_ID + "/"))).
        then().
            statusCode(Status.NO_CONTENT.getStatusCode()).
        when().
            delete(baseURL + "api/rel/{sourceObj}/{source}/{rel}/{targetObj}/",
                "sourceobject", SOURCE_ID, type, "targetobject");
    }

    @Test @InSequence(4)
    public void shouldNotBeAbleToFindRelationAfterDelete() throws Exception {
        given().
        then().
            statusCode(Status.NO_CONTENT.getStatusCode()).
        when().
            get(baseURL + "api/rel/{sourceObj}/{source}/{rel}/{targetObj}", "sourceobject", SOURCE_ID, type, "targetobject");
    }
}