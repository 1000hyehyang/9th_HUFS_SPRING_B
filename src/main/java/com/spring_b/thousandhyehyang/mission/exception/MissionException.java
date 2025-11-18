package com.spring_b.thousandhyehyang.mission.exception;

import com.spring_b.thousandhyehyang.global.exception.BaseException;

public class MissionException extends BaseException {

    public MissionException(MissionErrorCode errorCode) {
        super(errorCode);
    }

    public MissionException(MissionErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}

