package org.cedj.geekseek.web.rest.conference.test.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.cedj.geekseek.web.rest.conference.test.integration.ConferenceTypes.CONFERENCE_MEDIA_TYPE;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.model.Duration;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
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
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;

@WarpTest @RunAsClient
@RunWith(Arquillian.class)
public class ConferenceResourceTestCase {

    @Deployment
    public static WebArchive deploy() {
        return ConferenceRestDeployments.conference()
                .addAsWebInfResource(new File("src/test/resources/beans.xml"));
    }

    @ArquillianResource
    private URL baseURL;

    @BeforeClass
    public static void setup() {
        RestAssured.filters(
                ResponseLoggingFilter.responseLogger(),
                new RequestLoggingFilter());
    }

    @Test @InSequence(0)
    public void shouldBeAbleToGetSingleConference() throws Exception {
        final Conference conference = new Conference()
            .setName("Name")
            .setTagLine("TagName")
            .setDuration(new Duration(new Date(), new Date()));

        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                given().
                then().
                    contentType(CONFERENCE_MEDIA_TYPE).
                    root("conference").
                        body("name", equalTo(conference.getName())).
                        body("tagLine", equalTo(conference.getTagLine())).
                        body("start", equalToXmlDate(conference.getDuration().getStart())).
                        body("end", equalToXmlDate(conference.getDuration().getEnd())).
                        body("link.find {it.@rel == 'session'}.size()", equalTo(1)).
                when().
                    get(baseURL + "api/conference/{id}", conference.getId()).
                body();
            }
        }).inspect(new SetupConference(conference));
    }

    public static class SetupConference extends Inspection {
        private static final long serialVersionUID = 1L;

        private Conference conference;

        public SetupConference(Conference conference) {
            this.conference = conference;
        }

        @BeforeServlet
        public void store(Repository<Conference> repository) {
            repository.store(conference);
        }
    }

    private static Matcher<Object> equalToXmlDate(final Date date) {
        return new BaseMatcher<Object>() {

            @Override
            public boolean matches(Object item) {
                if(item == null) {
                    return false;
                }
                Date value = javax.xml.bind.DatatypeConverter.parseDateTime(item.toString()).getTime();
                return date.equals(value);
            }

            @Override
            public void describeTo(Description description) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                description.appendValue(
                        javax.xml.bind.DatatypeConverter.printDateTime(cal));
            }
        };
    }
}
