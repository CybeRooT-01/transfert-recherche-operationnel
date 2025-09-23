package com.transfert.transfert.Annotation;

import com.transfert.transfert.validation.UniquePhoneNumberValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniquePhoneNumberValidation.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePhoneNumber {
    String message() default "Ce numero de telephone est déjà pris";
        Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
