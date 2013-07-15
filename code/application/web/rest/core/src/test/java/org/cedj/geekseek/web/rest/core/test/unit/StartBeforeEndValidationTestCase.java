package org.cedj.geekseek.web.rest.core.test.unit;

import static org.cedj.geekseek.web.rest.core.test.unit.ValidationAssert.assertValidationConstraint;

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintViolation;
import javax.validation.UnexpectedTypeException;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.cedj.geekseek.web.rest.core.annotation.StartBeforeEnd;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StartBeforeEndValidationTestCase {

    private Validator validator;

    @Before
    public void setupValidationFactory() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void shouldOKEndAfterStart() throws Throwable {
        StartEndDate obj = new StartEndDate();
        obj.start = new Date();
        obj.end = new Date(System.currentTimeMillis() + 2000);

        Set<ConstraintViolation<StartEndDate>> result = validator.validate(obj);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void shouldOKNullEnd() throws Throwable {
        StartEndDate obj = new StartEndDate();
        obj.start = new Date();
        obj.end = null;

        Set<ConstraintViolation<StartEndDate>> result = validator.validate(obj);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void shouldOKNullStart() throws Throwable {
        StartEndDate obj = new StartEndDate();
        obj.start = null;
        obj.end = new Date();

        Set<ConstraintViolation<StartEndDate>> result = validator.validate(obj);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void shouldOKStartEndEqual() throws Throwable {
        Date date = new Date();
        StartEndDate obj = new StartEndDate();
        obj.start = date;
        obj.end = date;

        Set<ConstraintViolation<StartEndDate>> result = validator.validate(obj);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void shouldInvalidateEndBeforeStart() throws Throwable {
        StartEndDate obj = new StartEndDate();
        obj.start = new Date();
        obj.end = new Date(System.currentTimeMillis() - 200000);

        assertValidationConstraint(validator, obj, "EndBeforeStart", "end");
    }

    @Test
    public void shouldInvalidateEndBeforeStartOther() throws Throwable {
        StartEndOtherDate obj = new StartEndOtherDate();
        obj.startOther = new Date();
        obj.endOther = new Date(System.currentTimeMillis() - 200000);

        assertValidationConstraint(validator, obj, "EndBeforeStart", "endOther");
    }

    @Test(expected = ConstraintDeclarationException.class)
    public void shouldHandleMissingStartDate() throws Throwable {
        MissingStartDate obj = new MissingStartDate();
        obj.end = new Date();

        try {
            validator.validate(obj);
        } catch (ValidationException e) {
            throw e.getCause();
        }
    }

    @Test(expected = ConstraintDeclarationException.class)
    public void shouldHandleMissingEndDate() throws Throwable {
        MissingEndDate obj = new MissingEndDate();
        obj.start = new Date();

        try {
            validator.validate(obj);
        } catch (ValidationException e) {
            throw e.getCause();
        }
    }

    @Test(expected = UnexpectedTypeException.class)
    public void shouldHandleWrongType() throws Throwable {
        WrongType obj = new WrongType();
        obj.start = "";
        obj.end = "";

        try {
            validator.validate(obj);
        } catch (ValidationException e) {
            throw e.getCause();
        }
    }

    @StartBeforeEnd
    private static class MissingStartDate {
        @SuppressWarnings("unused")
        private Date end;
    }

    @StartBeforeEnd
    private static class MissingEndDate {
        @SuppressWarnings("unused")
        private Date start;
    }

    @StartBeforeEnd
    private static class WrongType {
        @SuppressWarnings("unused")
        private String start;
        @SuppressWarnings("unused")
        private String end;
    }

    @StartBeforeEnd
    private static class StartEndDate {
        @SuppressWarnings("unused")
        private Date start;
        @SuppressWarnings("unused")
        private Date end;
    }

    @StartBeforeEnd(start = "startOther", end = "endOther")
    private static class StartEndOtherDate {
        @SuppressWarnings("unused")
        private Date startOther;
        @SuppressWarnings("unused")
        private Date endOther;
    }
}
