package com.spring_b.thousandhyehyang.review.service;

import com.spring_b.thousandhyehyang.review.dto.ReviewCreateRequest;
import com.spring_b.thousandhyehyang.review.dto.ReviewResponse;
import com.spring_b.thousandhyehyang.review.entity.Review;
import com.spring_b.thousandhyehyang.review.entity.ReviewImage;
import com.spring_b.thousandhyehyang.review.repository.ReviewRepository;
import com.spring_b.thousandhyehyang.store.entity.Store;
import com.spring_b.thousandhyehyang.store.repository.StoreRepository;
import com.spring_b.thousandhyehyang.store.service.StoreService;
import com.spring_b.thousandhyehyang.user.entity.User;
import com.spring_b.thousandhyehyang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 리뷰 Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final StoreService storeService;

    /**
     * 리뷰 작성
     * 
     * @param userId 사용자 ID
     * @param request 리뷰 작성 요청
     * @return 작성된 리뷰 응답
     */
    @Transactional
    public ReviewResponse createReview(Long userId, ReviewCreateRequest request) {
        log.info("리뷰 작성 요청 - userId: {}, storeId: {}", userId, request.getStoreId());

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        // 가게 조회
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다: " + request.getStoreId()));

        // 중복 리뷰 체크
        Review existingReview = reviewRepository.findByUserIdAndStoreId(userId, request.getStoreId());
        if (existingReview != null) {
            throw new IllegalStateException("이미 해당 가게에 리뷰를 작성하셨습니다.");
        }

        // 리뷰 생성
        Review review = Review.builder()
                .user(user)
                .store(store)
                .rating(request.getRating())
                .content(request.getContent())
                .build();

        // 리뷰 이미지 추가
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<ReviewImage> reviewImages = new ArrayList<>();
            for (String imageUrl : request.getImageUrls()) {
                ReviewImage reviewImage = ReviewImage.builder()
                        .review(review)
                        .url(imageUrl)
                        .build();
                reviewImages.add(reviewImage);
            }
            review.setReviewImages(reviewImages);
        }

        // 리뷰 저장
        Review savedReview = reviewRepository.save(review);

        storeService.updateAverageRating(store.getStoreId());

        log.info("리뷰 작성 완료 - reviewId: {}", savedReview.getReviewId());

        return ReviewResponse.from(savedReview);
    }
}

