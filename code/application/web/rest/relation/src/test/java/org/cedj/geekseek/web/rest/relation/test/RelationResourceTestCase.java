package org.cedj.geekseek.web.rest.relation.test;

import static com.jayway.restassured.RestAssured.given;

import java.net.URL;

import javax.inject.Inject;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.relation.test.RelationDeployments;
import org.cedj.geekseek.domain.relation.test.model.SourceObject;
import org.cedj.geekseek.domain.relation.test.model.SourceRepository;
import org.cedj.geekseek.domain.relation.test.model.TargetObject;
import org.cedj.geekseek.domain.relation.test.model.TargetRepository;
import org.cedj.geekseek.domain.relation.test.model.TestRepository;
import org.cedj.geekseek.domain.test.CoreDeployments;
import org.cedj.geekseek.web.rest.core.test.RestCoreDeployments;
import org.cedj.geekseek.web.rest.relation.RelationResource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
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
    private static final String SOURCE_ID = "11";
    private static final String TARGET_ID = "1";
    
    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addPackage(RelationResource.class.getPackage())
            .addClasses(TestApplication.class)
            .addClasses(
                TargetObject.class, 
                SourceObject.class, 
                TestRepository.class, 
                SourceRepository.class, 
                TargetRepository.class)
            .addClass(TestRelationRepository.class)
            .addAsLibraries(RelationDeployments.relation(), CoreDeployments.core(), RestCoreDeployments.root())
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

    @Test
    public void should() throws Exception {
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                given().
                then().
                    contentType(BASE_XML_MEDIA_TYPE).
                when().
                    get(baseURL + "api/rel/{sourceObj}/{source}/{rel}/{targetObj}", "sourceobject", SOURCE_ID, type, "targetobject");
            }
        }).inspect(new ValueInjection(target, source));
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