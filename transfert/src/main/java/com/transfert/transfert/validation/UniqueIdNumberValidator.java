package com.transfert.transfert.validation;

import com.transfert.transfert.Annotation.UniqueIdNumber;
import com.transfert.transfert.Repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueIdNumberValidator implements ConstraintValidator<UniqueIdNumber, String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueIdNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(String idNumber, ConstraintValidatorContext context) {
        if (idNumber == null) {
            return true; //de toute façon notblank le fais
        }
        return !userRepository.existsByIdNumber(idNumber);
    }
}
