package com.spring_b.thousandhyehyang.store.exception;

import com.spring_b.thousandhyehyang.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StoreErrorCode implements BaseErrorCode {
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE400_1", "가게를 찾을 수 없습니다."),
    OWNER_NOT_FOUND(HttpStatus.FORBIDDEN, "STORE400_2", "주인(Owner) 권한이 없습니다."),
    OWNER_PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE400_3", "사장님 프로필을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}

