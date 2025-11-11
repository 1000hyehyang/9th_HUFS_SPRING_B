package com.spring_b.thousandhyehyang.review.service;

import com.spring_b.thousandhyehyang.review.dto.ReviewCreateRequest;
import com.spring_b.thousandhyehyang.review.dto.ReviewResponse;
import com.spring_b.thousandhyehyang.review.dto.ReviewSearchRequest;
import org.springframework.data.domain.Page;

public interface ReviewService {

    ReviewResponse createReview(Long userId, ReviewCreateRequest request);

    Page<ReviewResponse> getMyReviews(Long userId, ReviewSearchRequest request);
}
