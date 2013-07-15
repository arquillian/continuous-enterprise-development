package org.cedj.geekseek.web.rest.user.test.unit;

import org.cedj.geekseek.web.rest.core.test.unit.NotNullValidationTheory;
import org.cedj.geekseek.web.rest.user.model.UserRepresentation;
import org.junit.experimental.theories.DataPoint;

public class UserRepresentationValidationTestCase extends NotNullValidationTheory {

    @DataPoint
    public static String NOT_NULL_HANDLE = "handle";
    @DataPoint
    public static String NOT_NULL_NAME = "name";
    @DataPoint
    public static String NOT_NULL_BIO = "bio";

    @Override
    protected Class<?> getValidationClass() {
        return UserRepresentation.class;
    }
}
