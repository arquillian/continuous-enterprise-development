package org.cedj.geekseek.web.rest.conference.test.integration;

import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.util.Date;

import org.cedj.geekseek.domain.conference.model.Duration;
import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.web.rest.conference.test.model.SessionType;
import org.cedj.geekseek.web.rest.core.test.integration.resource.BaseRepositoryResourceSpecification;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

import com.jayway.restassured.specification.ResponseSpecification;

@WarpTest
@RunWith(Arquillian.class)
public class SessionResourceSpecificationTestCase extends BaseRepositoryResourceSpecification<Session, SessionType> {

    @Deployment
    public static WebArchive deploy() {
        return ConferenceRestDeployments.conference()
                .addPackage(BaseRepositoryResourceSpecification.class.getPackage())
                .addAsWebInfResource(new File("src/test/resources/beans.xml"));
    }

    public SessionResourceSpecificationTestCase() {
        super(Session.class);
    }

    @Override
    protected String getBaseMediaType() {
        return ConferenceTypes.BASE_MEDIA_TYPE;
    }

    @Override
    protected String getTypedMediaType() {
        return ConferenceTypes.SESSION_MEDIA_TYPE;
    }

    @Override
    protected String getURISegment() {
        return "session";
    }

    @Override
    protected Session createDomainObject() {
        return new Session()
            .setTitle("Title")
            .setOutline("Outline")
            .setDuration(new Duration(new Date(), new Date()));
    }

    @Override
    protected SessionType createUpdateRepresentation() {
        return new SessionType()
            .setTitle("Title 2")
            .setOutline("Outline 2")
            .setStart(new Date())
            .setEnd(new Date());
    }

    @Override
    protected ResponseSpecification responseValidation(ResponseSpecification spec, Session session) {
        return spec.
            root("session").
                body("title", equalTo(session.getTitle())).
                body("outline", equalTo(session.getOutline())).
                body("start", equalToXmlDate(session.getDuration().getStart())).
                body("end", equalToXmlDate(session.getDuration().getEnd()));
    }
}