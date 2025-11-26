package com.spring_b.thousandhyehyang.mission.dto;

import com.spring_b.thousandhyehyang.global.validation.ValidPage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionPageRequest {

    @ValidPage(message = "페이지 번호는 1 이상이어야 합니다")
    @Builder.Default
    private Integer page = 1;

    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
    @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다")
    @Builder.Default
    private Integer size = 10;
}

