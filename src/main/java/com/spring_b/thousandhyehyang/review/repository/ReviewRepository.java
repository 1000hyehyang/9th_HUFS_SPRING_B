package com.spring_b.thousandhyehyang.review.repository;

import com.spring_b.thousandhyehyang.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    /**
     * 사용자와 가게로 리뷰 조회
     */
    @Query("SELECT r FROM Review r " +
           "WHERE r.user.userId = :userId " +
           "AND r.store.storeId = :storeId " +
           "AND r.deletedAt IS NULL")
    Review findByUserIdAndStoreId(@Param("userId") Long userId, @Param("storeId") Long storeId);

    /**
     * 가게별 평균 평점 계산
     */
    @Query("SELECT AVG(r.rating) FROM Review r " +
           "WHERE r.store.storeId = :storeId " +
           "AND r.deletedAt IS NULL")
    Double calculateAverageRatingByStoreId(@Param("storeId") Long storeId);
}
