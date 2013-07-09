package org.cedj.geekseek.domain.conference.test.integration;

import java.io.File;

import javax.inject.Inject;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.conference.model.Session;
import org.cedj.geekseek.domain.conference.test.TestUtils;
import org.cedj.geekseek.domain.test.integration.BaseTransactionalSpecification;
import org.cedj.geekseek.domain.test.integration.CoreDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SessionRepositoryTransactionalTestCase extends
    BaseTransactionalSpecification<Session, Repository<Session>> {

    private static final String UPDATED_TITLE = "TEST UPDATED";

    public SessionRepositoryTransactionalTestCase() {
        super(Session.class);
    }

    // Given
    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addAsLibraries(
                ConferenceDeployments.conference().addClasses(ConferenceTestCase.class, TestUtils.class)
                    .addAsManifestResource(new StringAsset(
                        CoreDeployments.persistence().exportAsString()), "persistence.xml")
                    .addAsManifestResource(new File("src/main/resources/META-INF/beans.xml")))
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addClass(BaseTransactionalSpecification.class);
    }

    @Inject
    private Repository<Session> repository;

    @Override
    protected Session createNewDomainObject() {
        return TestUtils.createSession();
    }

    @Override
    protected void updateDomainObject(Session domain) {
        domain.setTitle(UPDATED_TITLE);
    }

    @Override
    protected void validateUpdatedDomainObject(Session domain) {
        Assert.assertEquals(UPDATED_TITLE, domain.getTitle());
    }

    @Override
    protected Repository<Session> getRepository() {
        return repository;
    }
}
