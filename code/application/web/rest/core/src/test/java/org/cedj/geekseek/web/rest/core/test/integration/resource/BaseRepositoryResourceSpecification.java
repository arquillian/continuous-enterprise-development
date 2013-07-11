package org.cedj.geekseek.web.rest.core.test.integration.resource;

import static com.jayway.restassured.RestAssured.given;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.core.Response;

import org.cedj.geekseek.domain.model.Identifiable;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;
import com.jayway.restassured.specification.ResponseSpecification;

@WarpTest
@RunAsClient
public abstract class BaseRepositoryResourceSpecification<DOMAIN extends Identifiable, REP extends Object> {

    @ArquillianResource
    private URL baseURL;

    private Class<DOMAIN> domainClass;

    public BaseRepositoryResourceSpecification(Class<DOMAIN> domainClass) {
        this.domainClass = domainClass;
    }

    public Class<DOMAIN> getDomainClass() {
        return this.domainClass;
    }

    protected abstract DOMAIN createDomainObject();
    protected abstract REP createUpdateRepresentation();

    protected abstract String getTypedMediaType();
    protected abstract String getBaseMediaType();

    protected abstract String getURISegment();

    protected abstract ResponseSpecification responseValidation(ResponseSpecification spec, DOMAIN domain);

    public URL getBaseURL() {
        return baseURL;
    }

    @BeforeClass
    public static void setup() {
        RestAssured.filters(
                ResponseLoggingFilter.responseLogger(),
                new RequestLoggingFilter());
    }

    @Test
    public void shouldReturnNotFoundOnGETMissingResource() throws Exception {
        given().
            contentType(getBaseMediaType()).
        then().
            statusCode(Response.Status.NOT_FOUND.getStatusCode()).
        when().
            get(createRootURL() + "/{id}", "MISSING");
    }

    @Test
    public void shouldReturnNotFoundOnDELETEMissingResource() throws Exception {
        given().
            contentType(getBaseMediaType()).
        then().
            statusCode(Response.Status.NOT_FOUND.getStatusCode()).
        when().
            delete(createRootURL() + "/{id}", "MISSING");
    }

    @Test
    public void shouldReturnBadRequestOnPUTMissingResource() throws Exception {
        given().
            contentType(getBaseMediaType()).
            content(createUpdateRepresentation()).
        then().
            statusCode(Response.Status.BAD_REQUEST.getStatusCode()).
        when().
            put(createRootURL() + "/{id}", "MISSING");
    }

    @Test
    public void shouldReturnOKOnGETResource() throws Exception {
        final DOMAIN domain = createDomainObject();

        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                responseValidation(
                    given().
                    then().
                        contentType(getTypedMediaType())
                , domain).
                when().
                    get(createRootURL() + "/{id}", domain.getId()).
                body();
            }
        }).inspect(new SetupRepository<DOMAIN>(getDomainClass(), domain));
    }

    protected URL createRootURL() {
        try {
            return new URL(baseURL, "api/" + getURISegment());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Matcher<Object> equalToXmlDate(final Date date) {
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
