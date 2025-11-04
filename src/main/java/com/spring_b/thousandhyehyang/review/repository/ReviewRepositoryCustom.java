package com.spring_b.thousandhyehyang.review.repository;

import com.spring_b.thousandhyehyang.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    
    /**
     * 내가 작성한 리뷰 조회
     * 
     * @param userId
     * @param storeName
     * @param ratingRange
     * @param pageable
     * @return Page<Review>
     */
    Page<Review> findMyReviews(Long userId, String storeName, Integer ratingRange, Pageable pageable);
}
