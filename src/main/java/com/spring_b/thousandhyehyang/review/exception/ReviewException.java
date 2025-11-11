package com.spring_b.thousandhyehyang.review.exception;

import com.spring_b.thousandhyehyang.global.exception.BaseException;

public class ReviewException extends BaseException {

    public ReviewException(ReviewErrorCode errorCode) {
        super(errorCode);
    }

    public ReviewException(ReviewErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}

