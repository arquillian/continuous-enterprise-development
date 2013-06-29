package org.cedj.geekseek.web.rest.relation.test.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.net.URL;

import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.relation.RelationRepository;
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
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.servlet.BeforeServlet;
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

    private static final String BASE_XML_MEDIA_TYPE = "application/vnd.ced+xml";
    private static final String BASE_JSON_MEDIA_TYPE = "application/vnd.ced+json";

    private static final String SOURCE_ID = "11";
    private static final String TARGET_ID = "1";

    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addPackage(RelationResource.class.getPackage())
            //.addPackages(false, Filters.exclude(TestRelationRepository.class), TestApplication.class.getPackage())
            .addPackage(TestApplication.class.getPackage())
            .addClasses(
                TargetObject.class,
                SourceObject.class,
                TestRepository.class,
                SourceRepository.class,
                TargetRepository.class)
            .addAsLibraries(CoreDeployments.core(), RestCoreDeployments.root())
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
                then().
                    contentType(BASE_JSON_MEDIA_TYPE).
                    statusCode(Status.OK.getStatusCode()).
                when().
                    put(baseURL + "api/rel/{sourceObj}/{source}/{rel}/{targetObj}/{target}",
                        "sourceobject", SOURCE_ID, type, "targetobject", TARGET_ID);
            }
        }).inspect(new ValueInjection(target, source));
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
        then().
            statusCode(Status.NO_CONTENT.getStatusCode()).
        when().
            delete(baseURL + "api/rel/{sourceObj}/{source}/{rel}/{targetObj}/{target}",
                "sourceobject", SOURCE_ID, type, "targetobject", TARGET_ID);
    }

    @Test @InSequence(4)
    public void shouldBeAbleToFindRelationAfterDelete() throws Exception {
        given().
        then().
            statusCode(Status.NO_CONTENT.getStatusCode()).
        when().
            get(baseURL + "api/rel/{sourceObj}/{source}/{rel}/{targetObj}", "sourceobject", SOURCE_ID, type, "targetobject");
    }

    private static class ValueInjection extends Inspection {
        private static final long serialVersionUID = 1L;

        @Inject
        Repository<TargetObject> targetRepo;

        @Inject
        Repository<SourceObject> sourceRepo;

        private TargetObject t;
        private SourceObject s;

        public ValueInjection(TargetObject t, SourceObject s) {
            this.t = t;
            this.s = s;
        }

        @BeforeServlet
        public void setup() {
            targetRepo.store(t);
            sourceRepo.store(s);
        }
    }
}