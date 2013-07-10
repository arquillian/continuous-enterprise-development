package org.cedj.geekseek.web.rest.user.test.integration;

import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.util.UUID;

import org.cedj.geekseek.domain.user.model.User;
import org.cedj.geekseek.web.rest.core.test.integration.resource.BaseRepositoryResourceSpecification;
import org.cedj.geekseek.web.rest.user.test.model.UserType;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

import com.jayway.restassured.specification.ResponseSpecification;

@WarpTest
@RunWith(Arquillian.class)
public class UserResourceSpecificationTestCase extends BaseRepositoryResourceSpecification<User, UserType> {

    @Deployment
    public static WebArchive deploy() {
        return UserRestDeployments.user()
                .addPackage(BaseRepositoryResourceSpecification.class.getPackage())
                .addAsWebInfResource(new File("src/test/resources/beans.xml"));
    }

    public UserResourceSpecificationTestCase() {
        super(User.class);
    }

    @Override
    protected String getBaseMediaType() {
        return UserTypes.BASE_MEDIA_TYPE;
    }

    @Override
    protected String getTypedMediaType() {
        return UserTypes.USER_MEDIA_TYPE;
    }

    @Override
    protected String getURISegment() {
        return "user";
    }

    @Override
    protected User createDomainObject() {
        return new User(UUID.randomUUID().toString())
            .setName("Name")
            .setBio("Bio");
    }

    @Override
    protected UserType createUpdateRepresentation() {
        return new UserType()
            .setName("Name 2")
            .setBio("Bio 2");
    }

    @Override
    protected ResponseSpecification responseValidation(ResponseSpecification spec, User user) {
        return spec.
            root("user").
                body("name", equalTo(user.getName())).
                body("bio", equalTo(user.getBio()));
        }
}