package com.spring_b.thousandhyehyang.review.exception;

import com.spring_b.thousandhyehyang.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements BaseErrorCode {
    REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "REVIEW400_1", "이미 해당 조건을 만족하는 리뷰가 존재합니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW400_2", "요청한 리뷰를 찾을 수 없습니다."),
    STORE_NOT_VISITED(HttpStatus.FORBIDDEN, "REVIEW400_3", "해당 가게를 이용한 이력이 없어 리뷰를 작성할 수 없습니다."),
    REPLY_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "REVIEW400_4", "리뷰 작성자 또는 가게 주인만 댓글을 작성할 수 있습니다."),
    PARENT_REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW400_5", "부모 댓글을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}

