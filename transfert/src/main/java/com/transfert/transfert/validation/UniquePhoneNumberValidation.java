package com.transfert.transfert.validation;

import com.transfert.transfert.Annotation.UniquePhoneNumber;
import com.transfert.transfert.Repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniquePhoneNumberValidation implements ConstraintValidator<UniquePhoneNumber, String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniquePhoneNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null) {
            return true; //de toute façon notblank le fais
        }
        return !userRepository.existsByPhoneNumber(phoneNumber);
    }
}
