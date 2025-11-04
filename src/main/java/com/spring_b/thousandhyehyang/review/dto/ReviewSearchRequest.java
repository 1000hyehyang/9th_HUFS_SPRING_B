package com.spring_b.thousandhyehyang.review.dto;

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
public class ReviewSearchRequest {

    private String storeName;

    @Min(value = 0, message = "별점 범위는 0 이상이어야 합니다")
    @Max(value = 5, message = "별점 범위는 5 이하여야 합니다")
    private Integer ratingRange;

    @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
    @Builder.Default
    private Integer page = 0;

    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
    @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다")
    @Builder.Default
    private Integer size = 10;

    // 정렬 기준 (createdAt, rating, updatedAt)
    private String sortBy;

    // 정렬 방향 (ASC, DESC)
    private String sortDirection;
}
