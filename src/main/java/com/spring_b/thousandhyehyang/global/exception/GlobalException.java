package com.spring_b.thousandhyehyang.global.exception;

import com.spring_b.thousandhyehyang.global.apiPayload.code.BaseErrorCode;

public class GlobalException extends BaseException {

    public GlobalException(BaseErrorCode errorCode) {
        super(errorCode);
    }

    public GlobalException(BaseErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}

