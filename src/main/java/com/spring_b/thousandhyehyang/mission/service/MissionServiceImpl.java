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
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;

    @Override
    public MissionPageResponse getAvailableMissionsByRegion(MissionSearchRequest request) {
        log.info("홈 화면 미션 조회 요청 - sido: {}, sigungu: {}, userId: {}",
                request.getSido(), request.getSigungu(), request.getUserId());

        Pageable pageable = createPageable(request);

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

    @Override
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

    @Override
    public UserMissionPageResponse getCompletedMissions(Long userId, Integer page, Integer size) {
        log.info("완료한 미션 조회 요청 - userId: {}, page: {}, size: {}", userId, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "completedAt"));

        Page<UserMission> userMissionPage = userMissionRepository.findByUserIdAndStatusOrderByCompletedAtDesc(
                userId, MissionStatus.COMPLETED, pageable);

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

    private Pageable createPageable(MissionSearchRequest request) {
        String sortBy = request.getSortBy() != null && !request.getSortBy().isEmpty()
                ? request.getSortBy()
                : "createdAt";

        Sort.Direction direction = "ASC".equalsIgnoreCase(request.getSortDirection())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortBy);

        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }
}

