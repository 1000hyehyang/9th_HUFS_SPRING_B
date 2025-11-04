package com.spring_b.thousandhyehyang.review.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring_b.thousandhyehyang.review.entity.QReview;
import com.spring_b.thousandhyehyang.review.entity.Review;
import com.spring_b.thousandhyehyang.store.entity.QStore;
import com.spring_b.thousandhyehyang.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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
        QUser user = QUser.user;

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

        // 페이징 적용하여 조회
        var query = queryFactory
                .selectFrom(review)
                .innerJoin(review.store, store).fetchJoin()
                .innerJoin(review.user, user).fetchJoin()
                .where(builder);

        // Pageable의 Sort 정보를 사용하여 정렬 적용
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                var property = order.getProperty();
                if ("createdAt".equals(property)) {
                    query.orderBy(order.isAscending() ?
                        review.createdAt.asc() : review.createdAt.desc());      
                } else if ("rating".equals(property)) {
                    query.orderBy(order.isAscending() ?
                        review.rating.asc() : review.rating.desc());
                }
            });
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
                .select(review.count())
                .from(review)
                .where(builder)
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(reviews, pageable, total);
    }
}
