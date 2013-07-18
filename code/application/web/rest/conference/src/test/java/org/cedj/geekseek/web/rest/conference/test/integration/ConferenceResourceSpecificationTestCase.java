package org.cedj.geekseek.web.rest.conference.test.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.util.Date;

import javax.ws.rs.core.Response;

import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Duration;
import org.cedj.geekseek.web.rest.conference.test.model.ConferenceType;
import org.cedj.geekseek.web.rest.conference.test.model.SessionType;
import org.cedj.geekseek.web.rest.core.test.integration.resource.BaseRepositoryResourceSpecification;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jayway.restassured.specification.ResponseSpecification;

@WarpTest
@RunWith(Arquillian.class)
public class ConferenceResourceSpecificationTestCase extends BaseRepositoryResourceSpecification<Conference, ConferenceType> {

    @Deployment
    public static WebArchive deploy() {
        return ConferenceRestDeployments.conference()
                .addPackage(BaseRepositoryResourceSpecification.class.getPackage())
                .addAsWebInfResource(new File("src/test/resources/beans.xml"));
    }

    public ConferenceResourceSpecificationTestCase() {
        super(Conference.class);
    }

    @Override
    protected String getBaseMediaType() {
        return ConferenceTypes.BASE_MEDIA_TYPE;
    }

    @Override
    protected String getTypedMediaType() {
        return ConferenceTypes.CONFERENCE_MEDIA_TYPE;
    }

    @Override
    protected String getURISegment() {
        return "conference";
    }

    @Override
    protected Conference createDomainObject() {
        return new Conference("Name", "TagLine", new Duration(new Date(), new Date()));
    }

    @Override
    protected ConferenceType createUpdateRepresentation() {
        return new ConferenceType()
            .setName("Name 2")
            .setTagLine("TagLine2")
            .setStart(new Date())
            .setEnd(new Date());
    }

    protected SessionType createSessionRepresentation() {
        return new SessionType()
            .setTitle("Title")
            .setOutline("Outline")
            .setStart(new Date())
            .setEnd(new Date());
    }

    @Override
    protected ResponseSpecification responseValidation(ResponseSpecification spec, Conference conference) {
        return spec.
            root("conference").
                body("name", equalTo(conference.getName())).
                body("tagLine", equalTo(conference.getTagLine())).
                body("start", equalToXmlDate(conference.getDuration().getStart())).
                body("end", equalToXmlDate(conference.getDuration().getEnd()));
    }

    @Test
    public void shouldReturnBadRequestOnMisingResourceWhenPOSTSession() throws Exception {
        given().
            contentType(getBaseMediaType()).
            content(createSessionRepresentation()).
        then().
            statusCode(Response.Status.BAD_REQUEST.getStatusCode()).
        when().
            post(createRootURL() + "/{id}/session", "MISSING");
    }
}