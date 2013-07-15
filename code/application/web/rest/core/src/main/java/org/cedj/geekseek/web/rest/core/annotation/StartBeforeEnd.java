package org.cedj.geekseek.web.rest.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.cedj.geekseek.web.rest.core.validation.StartBeforeEndValidator;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartBeforeEndValidator.class)
public @interface StartBeforeEnd {
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String message() default "End date is before Start";

    /**
     * @return The Field name for the start date
     */
    String start() default "start";

    /**
     * @return The Field name for the end date
     */
    String end() default "end";
}
