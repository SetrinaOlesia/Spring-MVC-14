package com.softserve.sprint14.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = StartBeforeEndDateValidator.class)
public @interface StartBeforeEndDateValidation {
    String message() default "{com.softserve.sprint13.validation.StartBeforeEndDateValidation.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
