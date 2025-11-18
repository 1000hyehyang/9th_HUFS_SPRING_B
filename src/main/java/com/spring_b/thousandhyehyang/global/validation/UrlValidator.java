package com.spring_b.thousandhyehyang.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlValidator implements ConstraintValidator<ValidUrl, String> {

    @Override
    public void initialize(ValidUrl constraintAnnotation) {
        // 초기화 로직이 필요한 경우 여기에 작성
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // null이면 검증 통과 (필수 여부는 @NotNull 등으로 처리)
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        try {
            new URL(value);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}