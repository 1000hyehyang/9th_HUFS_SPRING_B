package com.spring_b.thousandhyehyang.user.exception;

import com.spring_b.thousandhyehyang.global.exception.BaseException;

public class UserException extends BaseException {

    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(UserErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}

