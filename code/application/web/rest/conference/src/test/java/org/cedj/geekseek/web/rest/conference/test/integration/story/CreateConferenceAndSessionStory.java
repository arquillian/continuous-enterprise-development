package org.cedj.geekseek.web.rest.conference.test.integration.story;

import static com.jayway.restassured.RestAssured.given;
import static org.cedj.geekseek.web.rest.conference.test.integration.ConferenceTypes.BASE_MEDIA_TYPE;
import static org.cedj.geekseek.web.rest.conference.test.integration.ConferenceTypes.CONFERENCE_MEDIA_TYPE;
import static org.cedj.geekseek.web.rest.conference.test.integration.ConferenceTypes.SESSION_MEDIA_TYPE;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.Date;

import javax.ws.rs.core.Response.Status;

import org.cedj.geekseek.web.rest.conference.test.model.Conference;
import org.cedj.geekseek.web.rest.conference.test.model.Session;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;

@RunWith(Arquillian.class)
public class CreateConferenceAndSessionStory {

    private static String uri_conference = null;
    private static String uri_conferenceInstance = null;
    private static String uri_session = null;
    private static String uri_sessionInstance = null;

    @ArquillianResource
    private URL base;

    @BeforeClass
    public static void setup() {
        RestAssured.filters(
                ResponseLoggingFilter.responseLogger(),
                new RequestLoggingFilter());
    }

    // Story: As a 3. party Integrator I should be able locate the Conference root Resource

    @Test @InSequence(0)
    public void shouldBeAbleToLocateConferenceRoot() throws Exception {
        //uri_conference = new URL(base, "api/conference").toExternalForm();
        uri_conference =
              given().
              then().
                  contentType(BASE_MEDIA_TYPE).
                  statusCode(Status.OK.getStatusCode()).
                  root("root").
                      body("link.find {it.@rel == 'conference'}.size()", equalTo(1)).
              when().
                  get(new URL(base, "api/").toExternalForm()).
              body().
                  path("root.link.find {it.@rel == 'conference'}.@href");
    }

    // Story: As a 3. party Integrator I should be able create a Conference

    @Test @InSequence(1)
    public void shouldBeAbleToCreateConference() throws Exception {
        assertNotNull("Previous step failed", uri_conference);

        Conference conf = getCreateConference();

        uri_conferenceInstance =
              given().
                  contentType(CONFERENCE_MEDIA_TYPE).
                  body(conf).
              then().
                  statusCode(Status.CREATED.getStatusCode()).
              when().
                  post(uri_conference).
              header("Location");
    }

    // Story: As a 3. party Integrator I should be able get a Conference

    @Test @InSequence(2)
    public void shouldBeAbleToGetConference() throws Exception {
        assertNotNull("Previous step failed", uri_conferenceInstance);

        uri_session =
              given().
              then().
                  contentType(CONFERENCE_MEDIA_TYPE).
                  statusCode(Status.OK.getStatusCode()).
              when().
                  get(uri_conferenceInstance).
              body().
                  path("conference.link.find {it.@rel == 'session'}.@href");
    }

    // Story: As a 3. party Integrator I should be able update a Conference

    @Test @InSequence(3)
    public void shouldBeAbleToUpdateConference() throws Exception {
        assertNotNull("Previous step failed", uri_conferenceInstance);

        Conference conf = getUpdateConference();

        given().
            contentType(CONFERENCE_MEDIA_TYPE).
            body(conf).
        then().
            statusCode(Status.NO_CONTENT.getStatusCode()).
        when().
            put(uri_conferenceInstance);
    }

    @Test @InSequence(4)
    public void verifyUpdatedConference() throws Exception {
        assertNotNull("Previous step failed", uri_conferenceInstance);

        Conference conf = getUpdateConference();

        given().
        then().
           contentType(CONFERENCE_MEDIA_TYPE).
           statusCode(Status.OK.getStatusCode()).
           root("conference").
               body("name", equalTo(conf.getName())).
               body("tagLine", equalTo(conf.getTagLine())).
        when().
           get(uri_conferenceInstance);
    }

    // Story: As a 3. party Integrator I should be able create a Session for a Conference

    @Test @InSequence(5)
    public void shouldBeAbleToCreateSession() throws Exception {
        assertNotNull("Previous step failed", uri_session);

        Session session = getCreateSession();

        uri_sessionInstance =
              given().
                  contentType(SESSION_MEDIA_TYPE).
                  body(session).
              then().
                  statusCode(Status.CREATED.getStatusCode()).
              when().
                  post(uri_session).
              header("Location");
    }

    // Story: As a 3. party Integrator I should be able get a Session

    @Test @InSequence(6)
    public void shouldBeAbleToGetSession() throws Exception {
        assertNotNull("Previous step failed", uri_sessionInstance);

        given().
        then().
           contentType(SESSION_MEDIA_TYPE).
           statusCode(Status.OK.getStatusCode()).
        when().
           get(uri_sessionInstance);
    }

    // Story: As a 3. party Integrator I should be able update a Session

    @Test @InSequence(7)
    public void shouldBeAbleToUpdateSession() throws Exception {
        assertNotNull("Previous step failed", uri_sessionInstance);

        // TODO: require merge of models. merge == PATCH. PUT == full replacement
        Session session = getUpdateSession();

        given().
           contentType(SESSION_MEDIA_TYPE).
           body(session).
        then().
           statusCode(Status.NO_CONTENT.getStatusCode()).
        when().
           put(uri_sessionInstance);
    }

    @Test @InSequence(8)
    public void verifyUpdatedSession() throws Exception {
        assertNotNull("Previous step failed", uri_sessionInstance);

        Session session = getUpdateSession();

        given().
        then().
           contentType(SESSION_MEDIA_TYPE).
           statusCode(Status.OK.getStatusCode()).
           root("session").
               body("title", equalTo(session.getTitle())).
               body("outline", equalTo(session.getOutline())).
        when().
           get(uri_sessionInstance);
    }

    // Story: As a 3. party Integrator I should be able remove a Session from a Conference

    @Test @InSequence(8)
    public void shouldBeAbleToDeleteSession() throws Exception {
        assertNotNull("Previous step failed", uri_sessionInstance);

        given().
        then().
           statusCode(Status.NO_CONTENT.getStatusCode()).
        when().
           delete(uri_sessionInstance);
    }

    @Test @InSequence(9)
    public void verifyNotFoundForDeletedSession() throws Exception {
        assertNotNull("Previous step failed", uri_sessionInstance);

        given().
        then().
           statusCode(Status.NOT_FOUND.getStatusCode()).
        when().
           get(uri_sessionInstance);
    }

    // Story: As a 3. party Integrator I should be able remove a Conference

    @Test @InSequence(10)
    public void shouldBeAbleToDeleteConference() throws Exception {
        assertNotNull("Previous step failed", uri_conferenceInstance);

        given().
        then().
           statusCode(Status.NO_CONTENT.getStatusCode()).
        when().
           delete(uri_conferenceInstance);
    }

    @Test @InSequence(11)
    public void verifyNotFoundForDeletedConference() throws Exception {
        assertNotNull("Previous step failed", uri_conferenceInstance);

        given().
        then().
            // Few Containers support custom 404 media types. 404's are overwritten by default error page.
           //contentType(CONFERENCE_MEDIA_TYPE).
           statusCode(Status.NOT_FOUND.getStatusCode()).
        when().
           get(uri_conferenceInstance);
    }

    private Conference getCreateConference() {
        Conference conf = new Conference()
                                .setName("Test")
                                .setTagLine("Tagline")
                                .setStart(new Date())
                                .setEnd(new Date());
        return conf;
    }

    private Conference getUpdateConference() {
        Conference conf = new Conference()
                                .setName("Test 2")
                                .setTagLine("Tagline 2")
                                .setStart(new Date())
                                .setEnd(new Date());
        return conf;
    }

    private Session getCreateSession() {
        Session session = new Session()
                            .setTitle("Title")
                            .setOutline("Outline")
                            .setStart(new Date())
                            .setEnd(new Date());
        return session;
    }

    private Session getUpdateSession() {
        Session session = new Session()
                            .setTitle("Title 2")
                            .setOutline("Outline 2")
                            .setStart(new Date())
                            .setEnd(new Date());
        return session;
    }
}
