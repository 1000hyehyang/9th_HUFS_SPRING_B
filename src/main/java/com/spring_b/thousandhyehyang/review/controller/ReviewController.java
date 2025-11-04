package com.spring_b.thousandhyehyang.review.controller;

import com.spring_b.thousandhyehyang.review.dto.ReviewResponse;
import com.spring_b.thousandhyehyang.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * 
     * @param userId
     * @param storeName
     * @param ratingRange
     * @param page
     * @param size
     * @return Page<ReviewResponse>
     */
    @GetMapping("/my/{userId}")
    public ResponseEntity<Page<ReviewResponse>> getMyReviews(
            @PathVariable Long userId,
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) Integer ratingRange,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
                
        Page<ReviewResponse> reviews = reviewService.getMyReviews(userId, storeName, ratingRange, page, size);
        
        return ResponseEntity.ok(reviews);
    }
}

