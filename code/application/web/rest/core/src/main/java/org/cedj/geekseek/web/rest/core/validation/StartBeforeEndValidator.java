package org.cedj.geekseek.web.rest.core.validation;

import java.lang.reflect.Field;
import java.util.Date;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.UnexpectedTypeException;

import org.cedj.geekseek.web.rest.core.annotation.StartBeforeEnd;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, Object> {

    private StartBeforeEnd annotation;

    @Override
    public void initialize(StartBeforeEnd constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Class<?> type = value.getClass();

        Field startField = getField(annotation.start(), type);
        Field endField = getField(annotation.end(), type);

        Date startValue = getValue(startField, value);
        if(startValue == null) {
            return true;
        }
        Date endValue = getValue(endField, value);
        if(endValue == null) {
            return true;
        }

        if(endValue.before(startValue)) {
            context.buildConstraintViolationWithTemplate("EndBeforeStart")
                .addNode(annotation.end())
                .addConstraintViolation();
            return false;
        }
        return true;
    }

    private Field getField(String name, Class<?> type) {
        Field f;
        try {
            f = type.getDeclaredField(name);
        } catch(NoSuchFieldException e) {
            throw new ConstraintDeclarationException(
                "Could not validate " + StartBeforeEnd.class.getSimpleName()
                + ". No field found in " + type.getName() + " for " + name);
        }
        if(f.getType() != Date.class) {
            throw new UnexpectedTypeException(
                "Could not validate " + StartBeforeEnd.class.getSimpleName()
                + ". Field found in " + type.getName() + " for " + name + " is not a " + Date.class.getName());
        }
        return f;
    }

    private Date getValue(Field f, Object source) {
        if(!f.isAccessible()) {
            f.setAccessible(true);
        }
        try {
            return (Date)f.get(source);
        } catch (Exception e) {
            throw new RuntimeException(
                "Could not validate " + StartBeforeEnd.class.getSimpleName()
                + ". Could not get Field found in " + source.getClass().getName() + " with name " + f.getName(), e);
        }
    }
}
