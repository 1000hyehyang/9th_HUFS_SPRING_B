package com.spring_b.thousandhyehyang.point.repository;

import com.spring_b.thousandhyehyang.point.entity.PointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {

    /**
     * 사용자의 현재 포인트 조회
     */
    @Query("SELECT COALESCE(pt.balanceAfter, 0) FROM PointTransaction pt " +
           "WHERE pt.user.userId = :userId " +
           "AND pt.deletedAt IS NULL " +
           "AND pt.createdAt = (SELECT MAX(pt2.createdAt) FROM PointTransaction pt2 " +
           "WHERE pt2.user.userId = :userId AND pt2.deletedAt IS NULL)")
    Integer findCurrentBalanceByUserId(@Param("userId") Long userId);
}

