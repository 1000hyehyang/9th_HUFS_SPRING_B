package com.spring_b.thousandhyehyang.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMyPageResponse {

    private Long userId;
    private String nickname; // 이름 (닉네임)
    private String email; // 이메일
    private String phoneNumber; // 휴대폰 번호
    private Boolean phoneVerified; // 인증 여부
    private Integer currentPoints; // 현재 포인트
}

