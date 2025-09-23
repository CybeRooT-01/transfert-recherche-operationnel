package com.transfert.transfert.Annotation;

import com.transfert.transfert.validation.UniqueUsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {
    String message() default "Ce nom d'utilisateur est déjà pris";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}