package com.template.security.sanitizer.annotation;


import com.template.security.sanitizer.NoHtmlValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom annotation to validate that a string does not contain HTML or JavaScript content.
 * Can be applied to fields or method parameters.
 */
@Documented
@Constraint(validatedBy = NoHtmlValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoXSS {

    String message() default "HTML/JS not allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
