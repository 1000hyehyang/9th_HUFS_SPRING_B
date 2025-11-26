package com.spring_b.thousandhyehyang.review.service;

import com.spring_b.thousandhyehyang.mission.repository.UserMissionRepository;
import com.spring_b.thousandhyehyang.review.converter.ReviewConverter;
import com.spring_b.thousandhyehyang.review.dto.ReviewCreateRequest;
import com.spring_b.thousandhyehyang.review.dto.ReviewReplyCreateRequest;
import com.spring_b.thousandhyehyang.review.dto.ReviewReplyResponse;
import com.spring_b.thousandhyehyang.review.dto.ReviewResponse;
import com.spring_b.thousandhyehyang.review.dto.ReviewSearchRequest;
import com.spring_b.thousandhyehyang.review.entity.Review;
import com.spring_b.thousandhyehyang.review.entity.ReviewImage;
import com.spring_b.thousandhyehyang.review.entity.ReviewReply;
import com.spring_b.thousandhyehyang.review.exception.ReviewErrorCode;
import com.spring_b.thousandhyehyang.review.exception.ReviewException;
import com.spring_b.thousandhyehyang.review.repository.ReviewRepository;
import com.spring_b.thousandhyehyang.review.repository.ReviewReplyRepository;
import com.spring_b.thousandhyehyang.store.entity.Store;
import com.spring_b.thousandhyehyang.store.exception.StoreErrorCode;
import com.spring_b.thousandhyehyang.store.exception.StoreException;
import com.spring_b.thousandhyehyang.store.repository.StoreRepository;
import com.spring_b.thousandhyehyang.store.service.StoreService;
import com.spring_b.thousandhyehyang.user.entity.OwnerProfile;
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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewReplyRepository reviewReplyRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final StoreService storeService;
    private final UserMissionRepository userMissionRepository;

    @Override
    @Transactional
    public ReviewResponse createReview(Long userId, ReviewCreateRequest request) {
        log.info("리뷰 작성 요청 - userId: {}, storeId: {}", userId, request.getStoreId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        // 가게 이용 여부 검증: 해당 가게의 미션을 도전했는지 확인
        boolean hasVisitedStore = userMissionRepository.existsByUserIdAndStoreId(userId, request.getStoreId());
        if (!hasVisitedStore) {
            throw new ReviewException(ReviewErrorCode.STORE_NOT_VISITED);
        }

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
            List<ReviewImage> reviewImages = request.getImageUrls().stream()
                    .map(imageUrl -> ReviewImage.builder()
                            .review(review)
                            .url(imageUrl)
                            .build())
                    .collect(Collectors.toList());
            review.setReviewImages(reviewImages);
        }

        Review savedReview = reviewRepository.save(review);

        storeService.updateAverageRating(store.getStoreId());

        log.info("리뷰 작성 완료 - reviewId: {}, userId: {}, storeId: {}", 
                savedReview.getReviewId(), userId, request.getStoreId());

        return ReviewConverter.toResponse(savedReview);
    }

    @Override
    @Transactional
    public ReviewReplyResponse createReply(Long reviewId, ReviewReplyCreateRequest request) {
        log.info("리뷰 댓글 작성 요청 - reviewId: {}, userId: {}", reviewId, request.getUserId());

        // Review 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        // User 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 권한 검증: 리뷰 작성자 또는 가게 주인만 댓글 작성 가능
        boolean isReviewAuthor = review.getUser().getUserId().equals(request.getUserId());
        boolean isStoreOwner = false;

        if (!isReviewAuthor) {
            // 가게 주인인지 확인
            OwnerProfile ownerProfile = review.getStore().getOwner();
            if (ownerProfile != null && ownerProfile.getUser().getUserId().equals(request.getUserId())) {
                isStoreOwner = true;
            }

            if (!isStoreOwner) {
                throw new ReviewException(ReviewErrorCode.REPLY_NOT_AUTHORIZED);
            }
        }

        // 부모 댓글 조회 (대댓글인 경우)
        ReviewReply parentReply = null;
        if (request.getParentReplyId() != null) {
            parentReply = reviewReplyRepository.findById(request.getParentReplyId())
                    .orElseThrow(() -> new ReviewException(ReviewErrorCode.PARENT_REPLY_NOT_FOUND));

            // 부모 댓글이 같은 리뷰에 속하는지 확인
            if (!parentReply.getReview().getReviewId().equals(reviewId)) {
                throw new ReviewException(ReviewErrorCode.PARENT_REPLY_NOT_FOUND);
            }
        }

        // ReviewReply 엔티티 생성
        ReviewReply reviewReply = ReviewReply.builder()
                .review(review)
                .user(user)
                .parent(parentReply)
                .content(request.getContent().trim())
                .build();

        // ReviewReply 저장
        ReviewReply savedReply = reviewReplyRepository.save(reviewReply);

        log.info("리뷰 댓글 작성 완료 - replyId: {}, reviewId: {}, userId: {}", 
                savedReply.getReplyId(), reviewId, request.getUserId());

        return ReviewReplyResponse.builder()
                .replyId(savedReply.getReplyId())
                .reviewId(savedReply.getReview().getReviewId())
                .userId(savedReply.getUser().getUserId())
                .userNickname(savedReply.getUser().getNickname())
                .userRole(savedReply.getUser().getRole())
                .content(savedReply.getContent())
                .parentReplyId(parentReply != null ? parentReply.getReplyId() : null)
                .createdAt(savedReply.getCreatedAt())
                .updatedAt(savedReply.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getMyReviews(Long userId, ReviewSearchRequest request) {
        log.info("내가 작성한 리뷰 조회 요청 - userId: {}, request: {}", userId, request);

        Sort sort = createSort(request.getSortBy(), request.getSortDirection());
        // 프론트엔드는 1-based page를 전달하므로 0-based로 변환
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

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

