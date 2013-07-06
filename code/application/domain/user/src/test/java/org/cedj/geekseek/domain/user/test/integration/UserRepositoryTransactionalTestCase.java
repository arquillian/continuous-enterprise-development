package org.cedj.geekseek.domain.user.test.integration;

import java.io.File;
import java.util.UUID;

import javax.inject.Inject;

import org.cedj.geekseek.domain.Repository;
import org.cedj.geekseek.domain.test.integration.BaseTransactionalSpecification;
import org.cedj.geekseek.domain.test.integration.CoreDeployments;
import org.cedj.geekseek.domain.user.model.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class UserRepositoryTransactionalTestCase extends
    BaseTransactionalSpecification<User, Repository<User>> {

    private static final String UPDATED_NAME = "TEST UPDATED";

    public UserRepositoryTransactionalTestCase() {
        super(User.class);
    }

    // Given
    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addAsLibraries(
                UserDeployments.user()
                    .addAsManifestResource(new StringAsset(
                        CoreDeployments.persistence().exportAsString()), "persistence.xml")
                    .addAsManifestResource(new File("src/main/resources/META-INF/beans.xml")))
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addClass(BaseTransactionalSpecification.class);
    }

    @Inject
    private Repository<User> repository;

    @Override
    protected User createNewDomainObject() {
        return new User(UUID.randomUUID().toString()).setBio("Bio");
    }

    @Override
    protected void updateDomainObject(User domain) {
        domain.setName(UPDATED_NAME);
    }

    @Override
    protected void validateUpdatedDomainObject(User domain) {
        Assert.assertEquals(UPDATED_NAME, domain.getName());
    }

    @Override
    protected Repository<User> getRepository() {
        return repository;
    }
}
