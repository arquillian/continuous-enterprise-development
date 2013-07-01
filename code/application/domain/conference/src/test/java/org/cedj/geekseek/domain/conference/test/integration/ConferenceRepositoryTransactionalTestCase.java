package org.cedj.geekseek.domain.conference.test.integration;

import java.io.File;

import javax.inject.Inject;

import junit.framework.Assert;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.conference.model.Conference;
import org.cedj.geekseek.domain.conference.test.TestUtils;
import org.cedj.geekseek.domain.test.integration.BaseTransactionalBehavior;
import org.cedj.geekseek.domain.test.integration.CoreDeployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ConferenceRepositoryTransactionalTestCase extends
    BaseTransactionalBehavior<Conference, Repository<Conference>> {

    private static final String UPDATED_NAME = "TEST UPDATED";

    public ConferenceRepositoryTransactionalTestCase() {
        super(Conference.class);
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
            .addClass(BaseTransactionalBehavior.class);
    }

    @Inject
    private Repository<Conference> repository;

    @Override
    protected Conference createNewDomainObject() {
        return TestUtils.createConference();
    }

    @Override
    protected void updateDomainObject(Conference domain) {
        domain.setName(UPDATED_NAME);
    }

    @Override
    protected void validateUpdatedDomainObject(Conference domain) {
        Assert.assertEquals(UPDATED_NAME, domain.getName());
    }

    @Override
    protected Repository<Conference> getRepository() {
        return repository;
    }
}
