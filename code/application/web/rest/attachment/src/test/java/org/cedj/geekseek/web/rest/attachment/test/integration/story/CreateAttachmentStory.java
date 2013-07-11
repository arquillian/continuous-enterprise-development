package org.cedj.geekseek.web.rest.attachment.test.integration.story;

import static com.jayway.restassured.RestAssured.given;
import static org.cedj.geekseek.web.rest.attachment.test.integration.AttachmentTypes.ATTACHMENT_MEDIA_TYPE;
import static org.cedj.geekseek.web.rest.attachment.test.integration.AttachmentTypes.BASE_MEDIA_TYPE;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import javax.ws.rs.core.Response.Status;

import org.cedj.geekseek.web.rest.attachment.test.model.AttachmentType;
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
public class CreateAttachmentStory {

    private static String uri_attachment = null;
    private static String uri_attachmentInstance = null;

    @ArquillianResource
    private URL base;

    @BeforeClass
    public static void setup() {
        RestAssured.filters(
                ResponseLoggingFilter.responseLogger(),
                new RequestLoggingFilter());
    }

    // Story: As a 3. party Integrator I should be able locate the Attachment root Resource

    @Test @InSequence(0)
    public void shouldNotBeAbleToLocateAttachmentRoot() throws Exception {
              given().
              then().
                  contentType(BASE_MEDIA_TYPE).
                  statusCode(Status.OK.getStatusCode()).
                  root("root").
                      body("link.find {it.@rel == 'attachment'}.size()", equalTo(0)).
              when().
                  get(new URL(base, "api/").toExternalForm()).
              body();
    }

    // Story: As a 3. party Integrator I should be able create a Attachment

    @Test @InSequence(1)
    public void shouldBeAbleToCreateAttachment() throws Exception {
        // Attachment is not a top level resource, so in the test we hardcode the known location
        uri_attachment = new URL(base, "api/attachment").toExternalForm();

        AttachmentType conf = getCreateAttachment();

        uri_attachmentInstance =
              given().
                  contentType(ATTACHMENT_MEDIA_TYPE).
                  body(conf).
              then().
                  statusCode(Status.CREATED.getStatusCode()).
              when().
                  post(uri_attachment).
              header("Location");
    }

    // Story: As a 3. party Integrator I should be able get a Attachment

    @Test @InSequence(2)
    public void shouldBeAbleToGetAttachment() throws Exception {
        assertNotNull("Previous step failed", uri_attachmentInstance);

        given().
        then().
            contentType(ATTACHMENT_MEDIA_TYPE).
            statusCode(Status.OK.getStatusCode()).
        when().
            get(uri_attachmentInstance).
        body().
            path("attachment.link.find {it.@rel == 'session'}.@href");
    }

    // Story: As a 3. party Integrator I should be able update a Attachment

    @Test @InSequence(3)
    public void shouldBeAbleToUpdateAttachment() throws Exception {
        assertNotNull("Previous step failed", uri_attachmentInstance);

        AttachmentType conf = getUpdateAttachment();

        given().
            contentType(ATTACHMENT_MEDIA_TYPE).
            body(conf).
        then().
            statusCode(Status.NO_CONTENT.getStatusCode()).
        when().
            put(uri_attachmentInstance);
    }

    @Test @InSequence(4)
    public void verifyUpdatedAttachment() throws Exception {
        assertNotNull("Previous step failed", uri_attachmentInstance);

        AttachmentType conf = getUpdateAttachment();

        given().
        then().
           contentType(ATTACHMENT_MEDIA_TYPE).
           statusCode(Status.OK.getStatusCode()).
           root("attachment").
               body("title", equalTo(conf.getTitle())).
               body("mimeType", equalTo(conf.getMimeType())).
        when().
           get(uri_attachmentInstance);
    }

    // Story: As a 3. party Integrator I should be able remove a Attachment

    @Test @InSequence(10)
    public void shouldBeAbleToDeleteAttachment() throws Exception {
        assertNotNull("Previous step failed", uri_attachmentInstance);

        given().
        then().
           statusCode(Status.NO_CONTENT.getStatusCode()).
        when().
           delete(uri_attachmentInstance);
    }

    @Test @InSequence(11)
    public void verifyNotFoundForDeletedAttachment() throws Exception {
        assertNotNull("Previous step failed", uri_attachmentInstance);

        given().
        then().
            // Few Containers support custom 404 media types. 404's are overwritten by default error page.
           //contentType(CONFERENCE_MEDIA_TYPE).
           statusCode(Status.NOT_FOUND.getStatusCode()).
        when().
           get(uri_attachmentInstance);
    }

    private AttachmentType getCreateAttachment() throws Exception {
        AttachmentType attachment = new AttachmentType()
                                .setTitle("Test")
                                .setMimeType("text/plain")
                                .setUrl(new URL("http://geekseek.org"));
        return attachment;
    }

    private AttachmentType getUpdateAttachment() throws Exception {
        AttachmentType attachment = new AttachmentType()
                                .setTitle("Test 2")
                                .setMimeType("text/html")
                                .setUrl(new URL("http://geekseek2.org"));
        return attachment;
    }
}
