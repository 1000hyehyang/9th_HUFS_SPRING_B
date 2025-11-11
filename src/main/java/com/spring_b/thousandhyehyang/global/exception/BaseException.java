package com.spring_b.thousandhyehyang.global.exception;

import com.spring_b.thousandhyehyang.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final BaseErrorCode errorCode;
    private final String detailMessage;

    protected BaseException(BaseErrorCode errorCode) {
        this(errorCode, null);
    }

    protected BaseException(BaseErrorCode errorCode, String detailMessage) {
        super(detailMessage != null ? detailMessage : errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }
}

