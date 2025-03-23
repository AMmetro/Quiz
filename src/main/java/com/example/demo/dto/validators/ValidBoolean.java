package com.example.demo.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BooleanValidator.class) // Указываем валидатор, который будет обрабатывать эту аннотацию
@Target({ElementType.FIELD}) // Аннотация может использоваться для полей и параметров методов
@Retention(RetentionPolicy.RUNTIME) // Аннотация должна быть доступна во время выполнения
public @interface ValidBoolean {
    String message() default "Value must be a valid boolean";
    Class<?>[] groups() default {}; // Необходимая часть для поддержки Bean Validation API
    Class<? extends Payload>[] payload() default {}; // Используется для дополнительной информации (например, severity-level)
} 