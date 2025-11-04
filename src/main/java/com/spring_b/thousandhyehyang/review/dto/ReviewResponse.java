package com.spring_b.thousandhyehyang.review.dto;

import com.spring_b.thousandhyehyang.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long reviewId;
    private Long userId;
    private String userNickname;
    private Long storeId;
    private String storeName;
    private Double rating;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
    private List<String> imageUrls;

    public static ReviewResponse from(Review review) {
        if (review == null) {
            return null;
        }

        List<String> imageUrls = review.getReviewImages().stream()
                .map(img -> img.getUrl())
                .toList();

        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUser().getUserId())
                .userNickname(review.getUser().getNickname())
                .storeId(review.getStore().getStoreId())
                .storeName(review.getStore().getName())
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .imageUrls(imageUrls)
                .build();
    }
}

