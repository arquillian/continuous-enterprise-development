package org.cedj.geekseek.web.rest.user.test.integration.story;

import static com.jayway.restassured.RestAssured.given;
import static org.cedj.geekseek.web.rest.user.test.integration.UserTypes.BASE_MEDIA_TYPE;
import static org.cedj.geekseek.web.rest.user.test.integration.UserTypes.USER_MEDIA_TYPE;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import javax.ws.rs.core.Response.Status;

import org.cedj.geekseek.web.rest.user.test.model.UserType;
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
public class CreateUserStory {

    private static String uri_user = null;
    private static String uri_userInstance = null;

    @ArquillianResource
    private URL base;

    @BeforeClass
    public static void setup() {
        RestAssured.filters(
                ResponseLoggingFilter.responseLogger(),
                new RequestLoggingFilter());
    }

    // Story: As a 3. party Integrator I should be able locate the User root Resource

    @Test @InSequence(0)
    public void shouldNotBeAbleToLocateUserRoot() throws Exception {
              given().
              then().
                  contentType(BASE_MEDIA_TYPE).
                  statusCode(Status.OK.getStatusCode()).
                  root("root").
                      body("link.find {it.@rel == 'user'}.size()", equalTo(0)).
              when().
                  get(new URL(base, "api/").toExternalForm()).
              body();
    }

    // Story: As a 3. party Integrator I should be able create a User

    @Test @InSequence(1)
    public void shouldBeAbleToCreateUser() throws Exception {
        // User is not a top level resource, so in the test we hardcode the known location
        uri_user = new URL(base, "api/user").toExternalForm();

        UserType conf = getCreateUser();

        uri_userInstance =
              given().
                  contentType(USER_MEDIA_TYPE).
                  body(conf).
              then().
                  statusCode(Status.CREATED.getStatusCode()).
              when().
                  post(uri_user).
              header("Location");
    }

    // Story: As a 3. party Integrator I should be able get a User

    @Test @InSequence(2)
    public void shouldBeAbleToGetUser() throws Exception {
        assertNotNull("Previous step failed", uri_userInstance);

        given().
        then().
            contentType(USER_MEDIA_TYPE).
            statusCode(Status.OK.getStatusCode()).
        when().
            get(uri_userInstance);
    }

    // Story: As a 3. party Integrator I should be able update a User

    @Test @InSequence(3)
    public void shouldBeAbleToUpdateUser() throws Exception {
        assertNotNull("Previous step failed", uri_userInstance);

        UserType conf = getUpdateUser();

        given().
            contentType(USER_MEDIA_TYPE).
            body(conf).
        then().
            statusCode(Status.NO_CONTENT.getStatusCode()).
        when().
            put(uri_userInstance);
    }

    @Test @InSequence(4)
    public void verifyUpdatedUser() throws Exception {
        assertNotNull("Previous step failed", uri_userInstance);

        UserType conf = getUpdateUser();

        given().
        then().
           contentType(USER_MEDIA_TYPE).
           statusCode(Status.OK.getStatusCode()).
           root("user").
               body("name", equalTo(conf.getName())).
               body("bio", equalTo(conf.getBio())).
               body("handle", not(equalTo(conf.getHandle()))). // handle is the id, can not be updated
        when().
           get(uri_userInstance);
    }

    // Story: As a 3. party Integrator I should be able remove a User

    @Test @InSequence(10)
    public void shouldBeAbleToDeleteUser() throws Exception {
        assertNotNull("Previous step failed", uri_userInstance);

        given().
        then().
           statusCode(Status.NO_CONTENT.getStatusCode()).
        when().
           delete(uri_userInstance);
    }

    @Test @InSequence(11)
    public void verifyNotFoundForDeletedUser() throws Exception {
        assertNotNull("Previous step failed", uri_userInstance);

        given().
        then().
            // Few Containers support custom 404 media types. 404's are overwritten by default error page.
           //contentType(USER_MEDIA_TYPE).
           statusCode(Status.NOT_FOUND.getStatusCode()).
        when().
           get(uri_userInstance);
    }

    private UserType getCreateUser() throws Exception {
        UserType user = new UserType()
                                .setHandle("Handle")
                                .setName("Name")
                                .setBio("Bio");
        return user;
    }

    private UserType getUpdateUser() throws Exception {
        UserType user = new UserType()
                                .setHandle("Handle 2")
                                .setName("Name 2")
                                .setBio("Bio 2");
        return user;
    }
}
