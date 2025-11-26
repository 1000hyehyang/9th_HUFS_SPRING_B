package com.spring_b.thousandhyehyang.mission.service;

import com.spring_b.thousandhyehyang.mission.converter.MissionConverter;
import com.spring_b.thousandhyehyang.mission.dto.MissionCreateRequest;
import com.spring_b.thousandhyehyang.mission.dto.MissionPageResponse;
import com.spring_b.thousandhyehyang.mission.dto.MissionResponse;
import com.spring_b.thousandhyehyang.mission.dto.MissionSearchRequest;
import com.spring_b.thousandhyehyang.mission.dto.UserMissionChallengeRequest;
import com.spring_b.thousandhyehyang.mission.dto.UserMissionPageResponse;
import com.spring_b.thousandhyehyang.mission.dto.UserMissionResponse;
import com.spring_b.thousandhyehyang.mission.entity.Mission;
import com.spring_b.thousandhyehyang.mission.entity.UserMission;
import com.spring_b.thousandhyehyang.mission.entity.UserMissionId;
import com.spring_b.thousandhyehyang.mission.enums.MissionStatus;
import com.spring_b.thousandhyehyang.mission.exception.MissionErrorCode;
import com.spring_b.thousandhyehyang.mission.exception.MissionException;
import com.spring_b.thousandhyehyang.mission.repository.MissionRepository;
import com.spring_b.thousandhyehyang.mission.repository.UserMissionRepository;
import com.spring_b.thousandhyehyang.store.entity.Store;
import com.spring_b.thousandhyehyang.store.exception.StoreErrorCode;
import com.spring_b.thousandhyehyang.store.exception.StoreException;
import com.spring_b.thousandhyehyang.store.repository.StoreRepository;
import com.spring_b.thousandhyehyang.user.entity.OwnerProfile;
import com.spring_b.thousandhyehyang.user.entity.User;
import com.spring_b.thousandhyehyang.user.exception.UserErrorCode;
import com.spring_b.thousandhyehyang.user.exception.UserException;
import com.spring_b.thousandhyehyang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

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
    public MissionPageResponse getMissionsByStoreId(Long storeId, Integer page, Integer size) {
        log.info("특정 가게의 미션 목록 조회 요청 - storeId: {}, page: {}, size: {}", storeId, page, size);

        // 프론트엔드는 1-based page를 전달하므로 0-based로 변환
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Mission> missionPage = missionRepository.findByStoreId(storeId, pageable);

        List<MissionResponse> missionResponses = missionPage.getContent().stream()
                .map(MissionConverter::toMissionResponse)
                .toList();

        return MissionPageResponse.builder()
                .content(missionResponses)
                .page(missionPage.getNumber() + 1) // 1-based로 변환하여 반환
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

        // 프론트엔드는 1-based page를 전달하므로 0-based로 변환
        Pageable pageable = PageRequest.of(page - 1, size);

        Set<MissionStatus> statuses = Set.of(MissionStatus.IN_PROGRESS, MissionStatus.COMPLETED);
        Page<UserMission> userMissionPage = userMissionRepository.findByUserIdAndStatusIn(
                userId, statuses, pageable);

        List<UserMissionResponse> responses = userMissionPage.getContent().stream()
                .map(MissionConverter::toUserMissionResponse)
                .toList();

        return UserMissionPageResponse.builder()
                .content(responses)
                .page(userMissionPage.getNumber() + 1) // 1-based로 변환하여 반환
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
    public UserMissionPageResponse getInProgressMissions(Long userId, Integer page, Integer size) {
        log.info("내가 진행중인 미션 조회 요청 - userId: {}, page: {}, size: {}", userId, page, size);

        // 프론트엔드는 1-based page를 전달하므로 0-based로 변환
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Set<MissionStatus> statuses = Set.of(MissionStatus.IN_PROGRESS);
        Page<UserMission> userMissionPage = userMissionRepository.findByUserIdAndStatusIn(
                userId, statuses, pageable);

        List<UserMissionResponse> responses = userMissionPage.getContent().stream()
                .map(MissionConverter::toUserMissionResponse)
                .toList();

        return UserMissionPageResponse.builder()
                .content(responses)
                .page(userMissionPage.getNumber() + 1) // 1-based로 변환하여 반환
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

        // 프론트엔드는 1-based page를 전달하므로 0-based로 변환
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "completedAt"));

        Page<UserMission> userMissionPage = userMissionRepository.findByUserIdAndStatusOrderByCompletedAtDesc(
                userId, MissionStatus.COMPLETED, pageable);

        List<UserMissionResponse> responses = userMissionPage.getContent().stream()
                .map(MissionConverter::toUserMissionResponse)
                .toList();

        return UserMissionPageResponse.builder()
                .content(responses)
                .page(userMissionPage.getNumber() + 1) // 1-based로 변환하여 반환
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
    @Transactional
    public MissionResponse createMission(Long storeId, Long ownerId, MissionCreateRequest request) {
        // Store 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        // 권한 검증: 요청한 사용자가 해당 가게의 주인인지 확인
        OwnerProfile storeOwner = store.getOwner();
        if (storeOwner == null || !storeOwner.getUser().getUserId().equals(ownerId)) {
            throw new StoreException(StoreErrorCode.OWNER_NOT_FOUND);
        }

        // Mission 엔티티 생성
        Mission mission = Mission.builder()
                .store(store)
                .title(request.getTitle().trim())
                .description(request.getDescription().trim())
                .rewardPoint(request.getRewardPoint())
                .build();

        // Mission 저장
        Mission savedMission = missionRepository.save(mission);

        log.info("미션 생성 완료 - missionId: {}, title: {}", savedMission.getMissionId(), savedMission.getTitle());

        return MissionConverter.toMissionResponse(savedMission);
    }

    @Override
    @Transactional
    public UserMissionResponse challengeMission(Long missionId, UserMissionChallengeRequest request) {
        // Mission 조회
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new MissionException(MissionErrorCode.MISSION_NOT_FOUND));

        // User 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 이미 도전 중인 미션인지 확인
        UserMissionId userMissionId = new UserMissionId(request.getUserId(), missionId);
        Optional<UserMission> existingUserMission = userMissionRepository.findById(userMissionId);
        if (existingUserMission.isPresent() && existingUserMission.get().getDeletedAt() == null) {
            throw new MissionException(MissionErrorCode.USER_MISSION_ALREADY_EXISTS);
        }

        // UserMission 엔티티 생성
        UserMission userMission = UserMission.builder()
                .id(userMissionId)
                .user(user)
                .mission(mission)
                .status(MissionStatus.IN_PROGRESS)
                .build();

        // UserMission 저장
        UserMission savedUserMission = userMissionRepository.save(userMission);

        return MissionConverter.toUserMissionResponse(savedUserMission);
    }

    @Override
    @Transactional
    public UserMissionResponse completeMission(Long userId, Long missionId) {
        log.info("미션 완료 요청 - userId: {}, missionId: {}", userId, missionId);

        // UserMission 조회
        UserMissionId userMissionId = new UserMissionId(userId, missionId);
        UserMission userMission = userMissionRepository.findById(userMissionId)
                .orElseThrow(() -> new MissionException(MissionErrorCode.MISSION_NOT_FOUND));

        // 삭제된 미션인지 확인
        if (userMission.getDeletedAt() != null) {
            throw new MissionException(MissionErrorCode.MISSION_NOT_FOUND);
        }

        // 진행중인 미션인지 확인
        if (userMission.getStatus() != MissionStatus.IN_PROGRESS) {
            throw new MissionException(MissionErrorCode.MISSION_NOT_FOUND);
        }

        // 미션 완료 처리
        userMission.setStatus(MissionStatus.COMPLETED);
        userMission.setCompletedAt(Instant.now());

        log.info("미션 완료 처리 완료 - userId: {}, missionId: {}", userId, missionId);

        return MissionConverter.toUserMissionResponse(userMission);
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

