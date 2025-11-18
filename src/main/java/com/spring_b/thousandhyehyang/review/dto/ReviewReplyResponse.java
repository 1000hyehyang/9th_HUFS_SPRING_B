package com.spring_b.thousandhyehyang.review.dto;

import com.spring_b.thousandhyehyang.global.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewReplyResponse {

    private Long replyId;
    private Long reviewId;
    private Long userId;
    private String userNickname;
    private UserRole userRole;
    private String content;
    private Long parentReplyId;
    private Instant createdAt;
    private Instant updatedAt;
}

