package com.spring_b.thousandhyehyang.mission.repository;

import com.spring_b.thousandhyehyang.mission.entity.UserMission;
import com.spring_b.thousandhyehyang.mission.entity.UserMissionId;
import com.spring_b.thousandhyehyang.mission.enums.MissionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserMissionRepository extends JpaRepository<UserMission, UserMissionId> {

    /**
     * 내가 진행중, 진행 완료한 미션 조회
     */
    @Query(value = """
        SELECT um FROM UserMission um
        INNER JOIN FETCH um.mission m
        INNER JOIN FETCH m.store s
        WHERE um.user.userId = :userId
        AND um.status IN (:statuses)
        AND um.deletedAt IS NULL
        AND m.deletedAt IS NULL
        AND s.deletedAt IS NULL
        ORDER BY 
            CASE WHEN um.status = 'IN_PROGRESS' THEN 0 ELSE 1 END,
            COALESCE(um.completedAt, um.createdAt) DESC
        """,
        countQuery = """
        SELECT COUNT(um) FROM UserMission um
        WHERE um.user.userId = :userId
        AND um.status IN (:statuses)
        AND um.deletedAt IS NULL
        AND um.mission.deletedAt IS NULL
        """)
    Page<UserMission> findByUserIdAndStatusIn(
            @Param("userId") Long userId,
            @Param("statuses") Set<MissionStatus> statuses,
            Pageable pageable);

    /**
     * 사용자별 완료한 미션 조회 (완료일 기준 내림차순)
     */
    @Query(value = """
        SELECT um FROM UserMission um
        INNER JOIN FETCH um.mission m
        INNER JOIN FETCH m.store s
        WHERE um.user.userId = :userId
        AND um.status = :status
        AND um.deletedAt IS NULL
        AND m.deletedAt IS NULL
        AND s.deletedAt IS NULL
        ORDER BY um.completedAt DESC
        """,
        countQuery = """
        SELECT COUNT(um) FROM UserMission um
        WHERE um.user.userId = :userId
        AND um.status = :status
        AND um.deletedAt IS NULL
        AND um.mission.deletedAt IS NULL
        """)
    Page<UserMission> findByUserIdAndStatusOrderByCompletedAtDesc(
            @Param("userId") Long userId,
            @Param("status") MissionStatus status,
            Pageable pageable);

    /**
     * 사용자가 특정 가게의 미션을 도전했는지 확인
     */
    @Query("SELECT COUNT(um) > 0 FROM UserMission um " +
           "INNER JOIN um.mission m " +
           "WHERE um.user.userId = :userId " +
           "AND m.store.storeId = :storeId " +
           "AND um.deletedAt IS NULL " +
           "AND m.deletedAt IS NULL")
    boolean existsByUserIdAndStoreId(@Param("userId") Long userId, @Param("storeId") Long storeId);
}

