package com.spring_b.thousandhyehyang.review.controller;

import com.spring_b.thousandhyehyang.global.apiPayload.ApiResponse;
import com.spring_b.thousandhyehyang.global.apiPayload.code.GeneralSuccessCode;
import com.spring_b.thousandhyehyang.review.dto.ReviewResponse;
import com.spring_b.thousandhyehyang.review.dto.ReviewSearchRequest;
import com.spring_b.thousandhyehyang.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 내가 작성한 리뷰 보기 API (QueryDSL 사용)
     *
     * 필터링 조건:
     * - storeName: 가게명으로 필터링
     * - ratingRange: 별점 범위로 필터링
     * @param userId
     * @param request
     * @return Page<ReviewResponse>
     */
    @GetMapping("/my/{userId}")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getMyReviews(
            @PathVariable Long userId,
            @Valid @ModelAttribute ReviewSearchRequest request) {

        Page<ReviewResponse> reviews = reviewService.getMyReviews(userId, request);

        return ResponseEntity
                .status(GeneralSuccessCode.OK.getStatus())
                .body(ApiResponse.onSuccess(GeneralSuccessCode.OK, reviews));
    }
}

