package com.transfert.transfert.validation;

import com.transfert.transfert.Annotation.ExistInEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class ExistInEnumValidator implements ConstraintValidator<ExistInEnum, Enum<?>> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ExistInEnum annotation) {
        this.enumClass = annotation.enumClass();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) return true; // @NotNull gère ça
        return Arrays.asList(enumClass.getEnumConstants()).contains(value);
    }
}
