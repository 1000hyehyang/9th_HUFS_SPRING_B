package com.spring_b.thousandhyehyang.mission.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMissionChallengeRequest {

    @NotNull(message = "사용자 ID는 필수입니다")
    private Long userId;
}

