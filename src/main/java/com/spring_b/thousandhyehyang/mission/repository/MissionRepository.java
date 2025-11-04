package com.spring_b.thousandhyehyang.mission.repository;

import com.spring_b.thousandhyehyang.mission.entity.Mission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

    /**
     * 홈 화면: 현재 선택된 지역에서 도전 가능한 미션 목록 조회 (페이징 포함)
     * 
     * userId가 null인 경우: 모든 미션 조회
     * userId가 있는 경우: 사용자가 아직 도전하지 않은 미션만 조회
     */
    @Query(value = """
        SELECT DISTINCT m FROM Mission m
        INNER JOIN FETCH m.store s
        WHERE s.sido = :sido
        AND s.sigungu = :sigungu
        AND m.deletedAt IS NULL
        AND s.deletedAt IS NULL
        AND (:userId IS NULL OR m.missionId NOT IN (
            SELECT um.mission.missionId FROM UserMission um
            WHERE um.user.userId = :userId
            AND um.deletedAt IS NULL
        ))
        """
        ,
        countQuery = """
        SELECT COUNT(DISTINCT m) FROM Mission m
        INNER JOIN m.store s
        WHERE s.sido = :sido
        AND s.sigungu = :sigungu
        AND m.deletedAt IS NULL
        AND s.deletedAt IS NULL
        AND (:userId IS NULL OR m.missionId NOT IN (
            SELECT um.mission.missionId FROM UserMission um
            WHERE um.user.userId = :userId
            AND um.deletedAt IS NULL
        ))
        """)
    Page<Mission> findAvailableMissionsByRegion(
            @Param("sido") String sido,
            @Param("sigungu") String sigungu,
            @Param("userId") Long userId,
            Pageable pageable);
}
