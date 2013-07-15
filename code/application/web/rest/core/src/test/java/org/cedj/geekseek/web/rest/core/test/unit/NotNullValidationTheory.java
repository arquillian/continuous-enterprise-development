package org.cedj.geekseek.web.rest.core.test.unit;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * Validation helper class.
 *
 * Extend this Test if you need to validate @NotNull validations on an Object.
 *
 * This will scan sub class for @DataPoint public static String field = "not_null_field_name";
 * and generate a new version of getValidationClass() pr iteration.
 *
 * The new Object is created with all Fields set to dummy values expect the current
 * DataPoint field, which is null.
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 */
@RunWith(Theories.class)
public abstract class NotNullValidationTheory {

    private Validator validator;

    @Before
    public void setupValidationFactory() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    protected abstract Class<?> getValidationClass();

    protected Validator getValidator() {
        return validator;
    }

    @Theory
    public void shouldNotAllowNullValue(String field) throws Exception {
        Object obj = generateObject(getValidationClass(), getDataPoints(), field);
        assertValidationConstraint(obj, "NotNull", field);
    }

    private Object generateObject(Class<?> type, String[] fieldNames, String nullField) throws Exception {
        Object o = type.newInstance();

        for(String fieldName : fieldNames) {
            Object value = null;

            Field f = type.getDeclaredField(fieldName);
            if(!f.isAccessible()) {
                f.setAccessible(true);
            }
            // If this is not the Field under test generate a dummy value. Else it's null
            if(!fieldName.equals(nullField)) {
                value = generateValue(f);
            }
            f.set(o, value);
        }
        return o;
    }

    private Object generateValue(Field f) throws Exception {
        if(f.getType() == String.class) {
            return "Some String";
        }
        if(f.getType() == URL.class) {
            return new URL("http://geekseek.org");
        }
        if(f.getType() == Date.class) {
            return new Date();
        }
        throw new RuntimeException("Could not generate dummy value for type " + f.getType());
    }

    private String[] getDataPoints() throws Exception {
        List<String> result = new ArrayList<String>();
        Field[] fields = this.getClass().getDeclaredFields();
        for(Field field : fields) {
            if(field.isAnnotationPresent(DataPoint.class)) {
                result.add(String.valueOf(field.get(null))); // @DataPoint fields are Static
            }
        }
        return result.toArray(new String[0]);
    }

    protected void assertValidationConstraint(Object object, String type, String... properties) {
        ValidationAssert.assertValidationConstraint(getValidator(), object, type, properties);
    }
}
