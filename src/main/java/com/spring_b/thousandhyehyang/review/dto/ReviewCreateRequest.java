package com.spring_b.thousandhyehyang.review.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCreateRequest {

    @NotNull(message = "가게 ID는 필수입니다")
    private Long storeId;

    @NotNull(message = "평점은 필수입니다")
    @DecimalMin(value = "0.0", message = "평점은 0점 이상이어야 합니다")
    @DecimalMax(value = "5.0", message = "평점은 5점 이하여야 합니다")
    private Double rating;

    @NotBlank(message = "리뷰 내용은 필수입니다")
    @Size(max = 2000, message = "리뷰 내용은 2000자 이하여야 합니다")
    private String content;

    private List<String> imageUrls; // 리뷰 이미지 URL 목록
}

