package com.spring_b.thousandhyehyang.global.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = AgeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAge {
    String message() default "만 14세 이상 120세 이하만 가입 가능합니다";
    int min() default 14;
    int max() default 120;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}