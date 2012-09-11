package org.ced.domain.conference;

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import junit.framework.Assert;

import org.ced.domain.conference.model.Conference;
import org.ced.domain.conference.model.Duration;
import org.junit.Before;
import org.junit.Test;

public class DomainValidationTestCase {

    private Validator validator;

    @Before
    public void setupValidationFactory() {
	validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void shouldNotAllowNullName() {
	Conference conference = new Conference();
	assertValidationConsraint(conference, "NotNull", "name");
    }

    @Test
    public void shouldNotAllowNullDuration() {
	Conference conference = new Conference();
	assertValidationConsraint(conference, "NotNull", "duration");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowEndBeforeStart() {
	new Duration(new Date(), new Date(System.currentTimeMillis()-1000));
    }
    
    private <T> void assertValidationConsraint(T object, String type,  String... properties) {
	Set<ConstraintViolation<T>> errors = validator.validate(object);

	Assert.assertFalse("Expecting vaidation errors", errors.isEmpty());

	for (String property : properties) {
	    ConstraintViolation<T> validationFound = null;
	    for (ConstraintViolation<T> cv : errors) {
		if(property.equals(cv.getPropertyPath().toString())) {
		    validationFound = cv;
		    break;
		}
	    }
	    if(validationFound == null) {
		String msg = "Expected validation error on property [%s] but non found. Found errors [%s]";
		Assert.fail(String.format(msg, property, errors));
	    } else if(!validationFound.getMessageTemplate().contains(type)) {
		String msg = "Expected validation error on property [%s] of type [%s]. Found error [%s]";
		Assert.fail(String.format(msg, property, type, validationFound));
	    }
	}

    }
}
