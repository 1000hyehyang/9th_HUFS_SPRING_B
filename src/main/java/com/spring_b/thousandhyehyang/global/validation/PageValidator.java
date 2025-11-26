package com.spring_b.thousandhyehyang.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PageValidator implements ConstraintValidator<ValidPage, Integer> {

    @Override
    public void initialize(ValidPage constraintAnnotation) {
        // 초기화 로직이 필요한 경우 여기에 작성
    }

    @Override
    public boolean isValid(Integer page, ConstraintValidatorContext context) {
        // null이면 검증 통과 (필수 여부는 @NotNull 등으로 처리)
        if (page == null) {
            return true;
        }

        // 페이지 번호는 1 이상이어야 함
        return page >= 1;
    }
}

