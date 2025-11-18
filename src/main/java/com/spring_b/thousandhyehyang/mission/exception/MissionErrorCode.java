package com.spring_b.thousandhyehyang.mission.exception;

import com.spring_b.thousandhyehyang.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MissionErrorCode implements BaseErrorCode {
    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "MISSION400_1", "미션을 찾을 수 없습니다."),
    USER_MISSION_ALREADY_EXISTS(HttpStatus.CONFLICT, "MISSION400_2", "이미 도전 중인 미션입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}

