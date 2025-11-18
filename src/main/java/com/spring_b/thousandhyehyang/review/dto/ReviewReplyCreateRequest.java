package com.spring_b.thousandhyehyang.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewReplyCreateRequest {

    @NotNull(message = "사용자 ID는 필수입니다")
    private Long userId;

    @NotBlank(message = "댓글 내용은 필수입니다")
    @Size(max = 1000, message = "댓글 내용은 1000자 이하여야 합니다")
    private String content;

    private Long parentReplyId; // 대댓글인 경우 부모 댓글 ID
}

