package com.spring_b.thousandhyehyang.review.dto;

import com.spring_b.thousandhyehyang.global.enums.UserRole;
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
    private List<Reply> replies;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Reply {
        private Long replyId;
        private String content;
        private String writerNickname;
        private UserRole writerRole;
        private Instant createdAt;
        private Long parentReplyId;
    }
}
