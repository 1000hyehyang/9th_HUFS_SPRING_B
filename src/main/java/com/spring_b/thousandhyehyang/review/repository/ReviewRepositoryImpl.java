package com.spring_b.thousandhyehyang.review.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring_b.thousandhyehyang.review.entity.QReview;
import com.spring_b.thousandhyehyang.review.entity.QReviewImage;
import com.spring_b.thousandhyehyang.review.entity.QReviewReply;
import com.spring_b.thousandhyehyang.review.entity.Review;
import com.spring_b.thousandhyehyang.store.entity.QStore;
import com.spring_b.thousandhyehyang.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Review> findMyReviews(Long userId, String storeName, Integer ratingRange, Pageable pageable) {
        QReview review = QReview.review;
        QStore store = QStore.store;
        QUser user = QUser.user; // 리뷰 작성자용
        QUser replyUser = new QUser("replyUser"); // 답글 작성자용
        QReviewReply reviewReply = QReviewReply.reviewReply;
        QReviewReply parentReply = new QReviewReply("parentReply"); // 대댓글의 부모용 별칭
        QReviewImage reviewImage = QReviewImage.reviewImage;

        BooleanBuilder builder = new BooleanBuilder();

        // 사용자 ID가 일치하고 삭제되지 않은 리뷰
        builder.and(review.user.userId.eq(userId))
               .and(review.deletedAt.isNull());

        // 가게명 필터링 (부분 일치)
        if (storeName != null && !storeName.trim().isEmpty()) {
            builder.and(store.name.containsIgnoreCase(storeName.trim()));
        }

        // 별점 필터링
        if (ratingRange != null) {
            builder.and(review.rating.goe(ratingRange.doubleValue()))
                   .and(review.rating.lt(ratingRange + 1.0));
        }

        JPAQuery<Review> query = queryFactory
                .selectFrom(review)
                .innerJoin(review.store, store).fetchJoin()
                .innerJoin(review.user, user).fetchJoin()
                .leftJoin(review.reviewImages, reviewImage).fetchJoin()
                .leftJoin(review.reviewReplies, reviewReply).fetchJoin()
                .leftJoin(reviewReply.user, replyUser).fetchJoin()
                .leftJoin(reviewReply.parent, parentReply).fetchJoin()
                .where(builder)
                .distinct(); // fetchJoin으로 인한 중복 제거

        List<OrderSpecifier<?>> orderSpecifiers = convertToQueryDslOrder(pageable.getSort(), review);
        if (!orderSpecifiers.isEmpty()) {
            query.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
        } else {
            // 기본 정렬: 최신순
            query.orderBy(review.createdAt.desc());
        }

        List<Review> reviews = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회
        Long totalCount = queryFactory
                .select(review.countDistinct())
                .from(review)
                .where(builder)
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(reviews, pageable, total);
    }

    /**
     * 받은 Sort 객체를 QueryDSL로 변환
     *
     * @param sort Spring Data Sort 객체
     * @param review QReview 객체
     * @return QueryDSL OrderSpecifier 리스트
     */
    private List<OrderSpecifier<?>> convertToQueryDslOrder(Sort sort, QReview review) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (sort == null || !sort.isSorted()) {
            return orderSpecifiers;
        }

        // 여러 정렬 조건을 모두 처리
        for (Sort.Order order : sort) {
            String property = order.getProperty();
            com.querydsl.core.types.Order direction = order.isAscending()
                    ? com.querydsl.core.types.Order.ASC
                    : com.querydsl.core.types.Order.DESC;

            OrderSpecifier<?> orderSpecifier = null;
            switch (property) {
                case "createdAt":
                    orderSpecifier = new OrderSpecifier<>(direction, review.createdAt);
                    break;
                case "rating":
                    orderSpecifier = new OrderSpecifier<>(direction, review.rating);
                    break;
                case "updatedAt":
                    orderSpecifier = new OrderSpecifier<>(direction, review.updatedAt);
                    break;
                default:
                    // Service에서 이미 검증했지만, 안전장치로 처리
                    break;
            }

            if (orderSpecifier != null) {
                orderSpecifiers.add(orderSpecifier);
            }
        }

        return orderSpecifiers;
    }
}
