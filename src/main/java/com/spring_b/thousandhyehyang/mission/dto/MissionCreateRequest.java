package com.spring_b.thousandhyehyang.mission.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionCreateRequest {

    @NotBlank(message = "미션 제목은 필수입니다")
    @Size(max = 100, message = "미션 제목은 100자 이하여야 합니다")
    private String title;

    @NotBlank(message = "미션 설명은 필수입니다")
    @Size(max = 1000, message = "미션 설명은 1000자 이하여야 합니다")
    private String description;

    @NotNull(message = "보상 포인트는 필수입니다")
    @Min(value = 1, message = "보상 포인트는 1 이상이어야 합니다")
    @Max(value = 10000, message = "보상 포인트는 10000 이하여야 합니다")
    private Integer rewardPoint;
}

