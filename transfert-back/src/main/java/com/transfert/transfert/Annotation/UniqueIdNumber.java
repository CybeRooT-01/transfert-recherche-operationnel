package com.transfert.transfert.Annotation;
import com.transfert.transfert.validation.UniqueIdNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueIdNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueIdNumber {
    String message() default "Ce numero de carte d'identié est déjà pris";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
