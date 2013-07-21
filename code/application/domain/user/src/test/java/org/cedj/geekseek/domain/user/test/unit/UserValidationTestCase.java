package org.cedj.geekseek.domain.user.test.unit;

import java.lang.reflect.Method;

import org.cedj.geekseek.domain.persistence.model.BaseEntity;
import org.cedj.geekseek.domain.test.unit.TimestampableSpecification;
import org.cedj.geekseek.domain.user.model.User;

public class UserValidationTestCase extends TimestampableSpecification<User> {

    @Override
    protected User createInstance() throws Exception {
        return new User("");
    }

    @Override
    protected void forceCreated(User entity) throws Exception {
        // date set during object creation
    }

    @Override
    protected void forceUpdate(User entity) throws Exception {
        Method m = BaseEntity.class.getDeclaredMethod("onUpdate");
        m.setAccessible(true);
        m.invoke(entity);
    }
}
