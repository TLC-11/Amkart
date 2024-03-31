package com.amkart.estore.validations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Documented
@Constraint(validatedBy = {AboutContentCheck.class})
@Target({ FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface AboutValidator {
	String message() default "irrelevant about";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}


