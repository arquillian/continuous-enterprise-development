package org.cedj.geekseek.web.rest.core.test.unit;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Assert;

public final class ValidationAssert {

    private ValidationAssert() {}

    public static <T> void assertValidationConstraint(Validator validator, T object, String type, String... properties) {
        Set<ConstraintViolation<T>> errors = validator.validate(object);

        Assert.assertFalse("Expecting validation errors for proeprties [" + join(properties) + "]", errors.isEmpty());

        for (String property : properties) {
            ConstraintViolation<T> validationFound = null;
            for (ConstraintViolation<T> cv : errors) {
                if (property.equals(cv.getPropertyPath().toString())) {
                    validationFound = cv;
                    break;
                }
            }
            if (validationFound == null) {
                String msg = "Expected validation error on property [%s] but non found. Found errors [%s]";
                Assert.fail(String.format(msg, property, errors));
            } else if (!validationFound.getMessageTemplate().contains(type)) {
                String msg = "Expected validation error on property [%s] of type [%s]. Found error [%s]";
                Assert.fail(String.format(msg, property, type, validationFound));
            }
        }
    }

    static String join(String... items) {
       StringBuilder sb = new StringBuilder();
       boolean first = true;
       for (String item : items) {
          if (first)
             first = false;
          else
             sb.append(", ");
          sb.append(item);
       }
       return sb.toString();
    }
}
