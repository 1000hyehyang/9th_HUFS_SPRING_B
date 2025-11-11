package com.spring_b.thousandhyehyang.review.converter;

import com.spring_b.thousandhyehyang.review.dto.ReviewResponse;
import com.spring_b.thousandhyehyang.review.dto.ReviewResponse.Reply;
import com.spring_b.thousandhyehyang.review.entity.Review;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class ReviewConverter {

    private ReviewConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static ReviewResponse toResponse(Review review) {
        if (review == null) {
            return null;
        }

        List<String> imageUrls = review.getReviewImages().stream()
                .filter(img -> img.getDeletedAt() == null)
                .map(reviewImage -> reviewImage.getUrl())
                .collect(Collectors.toList());

        List<Reply> replies = review.getReviewReplies().stream()
                .filter(reply -> reply.getDeletedAt() == null)
                .map(reply -> Reply.builder()
                        .replyId(reply.getReplyId())
                        .content(reply.getContent())
                        .writerNickname(reply.getUser().getNickname())
                        .writerRole(reply.getUser().getRole())
                        .createdAt(reply.getCreatedAt())
                        .parentReplyId(reply.getParent() != null ? reply.getParent().getReplyId() : null)
                        .build())
                .sorted(Comparator.comparing(Reply::getCreatedAt))
                .collect(Collectors.toList());

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
                .replies(replies)
                .build();
    }
}

