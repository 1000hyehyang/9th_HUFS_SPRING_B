package com.spring_b.thousandhyehyang.review.dto;

import com.spring_b.thousandhyehyang.global.enums.UserRole;
import com.spring_b.thousandhyehyang.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<Reply> replies;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Reply {
        private Long replyId;
        private String content;
        private String writerNickname;
        private UserRole writerRole; // OWNER, USER, ADMIN
        private Instant createdAt;
        private Long parentReplyId; // 대댓글인 경우 부모 댓글 ID, 원댓글인 경우 null
    }

    public static ReviewResponse from(Review review) {
        if (review == null) {
            return null;
        }

        // 삭제되지 않은 ReviewImage만 포함
        List<String> imageUrls = review.getReviewImages().stream()
                .filter(img -> img.getDeletedAt() == null)
                .map(img -> img.getUrl())
                .collect(Collectors.toList());

        // 삭제되지 않은 모든 답글들
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

