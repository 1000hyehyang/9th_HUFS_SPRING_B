package com.spring_b.thousandhyehyang.review.service;

import com.spring_b.thousandhyehyang.review.converter.ReviewConverter;
import com.spring_b.thousandhyehyang.review.dto.ReviewCreateRequest;
import com.spring_b.thousandhyehyang.review.dto.ReviewResponse;
import com.spring_b.thousandhyehyang.review.dto.ReviewSearchRequest;
import com.spring_b.thousandhyehyang.review.entity.Review;
import com.spring_b.thousandhyehyang.review.entity.ReviewImage;
import com.spring_b.thousandhyehyang.review.exception.ReviewErrorCode;
import com.spring_b.thousandhyehyang.review.exception.ReviewException;
import com.spring_b.thousandhyehyang.review.repository.ReviewRepository;
import com.spring_b.thousandhyehyang.store.entity.Store;
import com.spring_b.thousandhyehyang.store.exception.StoreErrorCode;
import com.spring_b.thousandhyehyang.store.exception.StoreException;
import com.spring_b.thousandhyehyang.store.repository.StoreRepository;
import com.spring_b.thousandhyehyang.store.service.StoreService;
import com.spring_b.thousandhyehyang.user.entity.User;
import com.spring_b.thousandhyehyang.user.exception.UserErrorCode;
import com.spring_b.thousandhyehyang.user.exception.UserException;
import com.spring_b.thousandhyehyang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final StoreService storeService;

    @Override
    @Transactional
    public ReviewResponse createReview(Long userId, ReviewCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        Review existingReview = reviewRepository.findByUserIdAndStoreId(userId, request.getStoreId());
        if (existingReview != null) {
            throw new ReviewException(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
        }

        Review review = Review.builder()
                .user(user)
                .store(store)
                .rating(request.getRating())
                .content(request.getContent())
                .build();

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

        Review savedReview = reviewRepository.save(review);

        storeService.updateAverageRating(store.getStoreId());

        return ReviewConverter.toResponse(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getMyReviews(Long userId, ReviewSearchRequest request) {
        log.info("내가 작성한 리뷰 조회 요청 - userId: {}, request: {}", userId, request);

        Sort sort = createSort(request.getSortBy(), request.getSortDirection());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<Review> reviewPage = reviewRepository.findMyReviews(
                userId,
                request.getStoreName(),
                request.getRatingRange(),
                pageable
        );

        List<ReviewResponse> reviewResponses = reviewPage.getContent().stream()
                .map(ReviewConverter::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(reviewResponses, pageable, reviewPage.getTotalElements());
    }

    private Sort createSort(String sortBy, String sortDirection) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        String property;
        switch (sortBy.toLowerCase()) {
            case "createdat":
            case "created_at":
                property = "createdAt";
                break;
            case "rating":
                property = "rating";
                break;
            case "updatedat":
            case "updated_at":
                property = "updatedAt";
                break;
            default:
                property = "createdAt";
                break;
        }

        return Sort.by(direction, property);
    }
}

