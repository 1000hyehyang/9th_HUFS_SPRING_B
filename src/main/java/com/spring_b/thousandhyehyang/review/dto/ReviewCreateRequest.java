package com.spring_b.thousandhyehyang.review.dto;

import com.spring_b.thousandhyehyang.global.validation.ValidUrl;
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

    @Size(max = 10, message = "이미지는 최대 10개까지 첨부할 수 있습니다")
    private List<@ValidUrl(message = "올바른 이미지 URL 형식이 아닙니다") String> imageUrls; // 리뷰 이미지 URL 목록
}

