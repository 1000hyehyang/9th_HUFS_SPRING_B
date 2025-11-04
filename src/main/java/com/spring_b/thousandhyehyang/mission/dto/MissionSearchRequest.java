package com.spring_b.thousandhyehyang.mission.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionSearchRequest {

    @NotBlank(message = "시/도는 필수입니다")
    private String sido;

    @NotBlank(message = "시/군/구는 필수입니다")
    private String sigungu;

    private Long userId;

    @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
    @Builder.Default
    private Integer page = 0;

    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
    @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다")
    @Builder.Default
    private Integer size = 10;

    // 정렬 기준 (createdAt, rewardPoint)
    private String sortBy;

    // 정렬 방향 (ASC, DESC)
    private String sortDirection;
}

