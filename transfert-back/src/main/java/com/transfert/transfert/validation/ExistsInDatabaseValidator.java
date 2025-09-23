package com.transfert.transfert.validation;

import com.transfert.transfert.Annotation.ExistInDatabase;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistsInDatabaseValidator implements ConstraintValidator<ExistInDatabase, Long> {
    @Autowired
    private EntityManager entityManager;

    private Class<?> entity;
    private String fieldName;

    @Override
    public void initialize(ExistInDatabase constraintAnnotation) {
        this.entity = constraintAnnotation.entity();
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) return true;
        String jpql = "SELECT COUNT(e) FROM " + entity.getSimpleName() + " e WHERE e." + fieldName + " = :value";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("value", value)
                .getSingleResult();
        return count > 0;
    }
}
