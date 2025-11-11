package com.spring_b.thousandhyehyang.mission.service;

import com.spring_b.thousandhyehyang.mission.converter.MissionConverter;
import com.spring_b.thousandhyehyang.mission.dto.MissionPageResponse;
import com.spring_b.thousandhyehyang.mission.dto.MissionResponse;
import com.spring_b.thousandhyehyang.mission.dto.MissionSearchRequest;
import com.spring_b.thousandhyehyang.mission.dto.UserMissionPageResponse;
import com.spring_b.thousandhyehyang.mission.dto.UserMissionResponse;
import com.spring_b.thousandhyehyang.mission.entity.Mission;
import com.spring_b.thousandhyehyang.mission.entity.UserMission;
import com.spring_b.thousandhyehyang.mission.enums.MissionStatus;
import com.spring_b.thousandhyehyang.mission.repository.MissionRepository;
import com.spring_b.thousandhyehyang.mission.repository.UserMissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;

    /**
     * 홈 화면: 현재 선택된 지역에서 도전 가능한 미션 목록 조회
     * 
     * @param request 검색 요청 (지역 정보, 페이징 정보)
     * @return 페이징된 미션 목록
     */
    public MissionPageResponse getAvailableMissionsByRegion(MissionSearchRequest request) {
        log.info("홈 화면 미션 조회 요청 - sido: {}, sigungu: {}, userId: {}", 
                request.getSido(), request.getSigungu(), request.getUserId());

        // 페이징 및 정렬 설정
        Pageable pageable = createPageable(request);

        // 도전 가능한 미션 조회
        Page<Mission> missionPage = missionRepository.findAvailableMissionsByRegion(
                request.getSido(),
                request.getSigungu(),
                request.getUserId(),
                pageable
        );

        List<MissionResponse> missionResponses = missionPage.getContent().stream()
                .map(MissionConverter::toMissionResponse)
                .toList();

        return MissionPageResponse.builder()
                .content(missionResponses)
                .page(missionPage.getNumber())
                .size(missionPage.getSize())
                .totalElements(missionPage.getTotalElements())
                .totalPages(missionPage.getTotalPages())
                .hasNext(missionPage.hasNext())
                .hasPrevious(missionPage.hasPrevious())
                .isFirst(missionPage.isFirst())
                .isLast(missionPage.isLast())
                .build();
    }

    /**
     * 페이징 및 정렬 정보를 기반으로 Pageable 생성
     */
    private Pageable createPageable(MissionSearchRequest request) {
        // 정렬 기준 설정
        String sortBy = request.getSortBy() != null && !request.getSortBy().isEmpty() 
                ? request.getSortBy() 
                : "createdAt"; // 기본값: 최신순

        // 정렬 방향 설정
        Sort.Direction direction = "ASC".equalsIgnoreCase(request.getSortDirection())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC; // 기본값: 내림차순

        Sort sort = Sort.by(direction, sortBy);
        
        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }

    /**
     * 내가 진행중, 진행 완료한 미션 모아서 조회
     * 
     * @param userId 사용자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 진행중+완료 미션 목록
     */
    public UserMissionPageResponse getMyMissions(Long userId, Integer page, Integer size) {
        log.info("내 미션 조회 요청 - userId: {}, page: {}, size: {}", userId, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Set<MissionStatus> statuses = Set.of(MissionStatus.IN_PROGRESS, MissionStatus.COMPLETED);
        Page<UserMission> userMissionPage = userMissionRepository.findByUserIdAndStatusIn(
                userId, statuses, pageable);

        List<UserMissionResponse> responses = userMissionPage.getContent().stream()
                .map(MissionConverter::toUserMissionResponse)
                .toList();

        return UserMissionPageResponse.builder()
                .content(responses)
                .page(userMissionPage.getNumber())
                .size(userMissionPage.getSize())
                .totalElements(userMissionPage.getTotalElements())
                .totalPages(userMissionPage.getTotalPages())
                .hasNext(userMissionPage.hasNext())
                .hasPrevious(userMissionPage.hasPrevious())
                .isFirst(userMissionPage.isFirst())
                .isLast(userMissionPage.isLast())
                .build();
    }

    /**
     * 내가 완료한 미션 목록 조회
     * 
     * @param userId 사용자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 완료한 미션 목록
     */
    public UserMissionPageResponse getCompletedMissions(Long userId, Integer page, Integer size) {
        log.info("완료한 미션 조회 요청 - userId: {}, page: {}, size: {}", userId, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "completedAt"));

        // 완료한 미션 조회 (@Query 어노테이션 사용)
        Page<UserMission> userMissionPage = userMissionRepository.findByUserIdAndStatusOrderByCompletedAtDesc(
                userId, MissionStatus.COMPLETED, pageable);

        // DTO 변환
        List<UserMissionResponse> responses = userMissionPage.getContent().stream()
                .map(MissionConverter::toUserMissionResponse)
                .toList();

        return UserMissionPageResponse.builder()
                .content(responses)
                .page(userMissionPage.getNumber())
                .size(userMissionPage.getSize())
                .totalElements(userMissionPage.getTotalElements())
                .totalPages(userMissionPage.getTotalPages())
                .hasNext(userMissionPage.hasNext())
                .hasPrevious(userMissionPage.hasPrevious())
                .isFirst(userMissionPage.isFirst())
                .isLast(userMissionPage.isLast())
                .build();
    }
}

