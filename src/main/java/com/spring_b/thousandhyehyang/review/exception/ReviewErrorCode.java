package com.spring_b.thousandhyehyang.review.exception;

import com.spring_b.thousandhyehyang.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements BaseErrorCode {
    REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "REVIEW400_1", "이미 해당 조건을 만족하는 리뷰가 존재합니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW400_2", "요청한 리뷰를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}

