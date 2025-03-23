package com.example.demo.dto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BooleanValidator implements ConstraintValidator<ValidBoolean, Object> {
    
    @Override
    public void initialize(ValidBoolean constraintAnnotation) {
        // Инициализация не требуется
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // Проверяем, что значение не null и является именно Boolean
        return value != null && value instanceof Boolean;
    }
} 