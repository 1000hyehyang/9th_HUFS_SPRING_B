package com.spring_b.thousandhyehyang.store.exception;

import com.spring_b.thousandhyehyang.global.exception.BaseException;

public class StoreException extends BaseException {

    public StoreException(StoreErrorCode errorCode) {
        super(errorCode);
    }

    public StoreException(StoreErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}

