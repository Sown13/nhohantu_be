package com.nhohantu.tcbookbe.common.annotation;

import com.nhohantu.tcbookbe.common.validation.RoleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsValidRole {
    String message() default "Invalid ROLE";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
