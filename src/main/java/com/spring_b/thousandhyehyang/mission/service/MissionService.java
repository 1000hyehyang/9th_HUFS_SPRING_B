package com.spring_b.thousandhyehyang.mission.service;

import com.spring_b.thousandhyehyang.mission.dto.MissionCreateRequest;
import com.spring_b.thousandhyehyang.mission.dto.MissionPageResponse;
import com.spring_b.thousandhyehyang.mission.dto.MissionResponse;
import com.spring_b.thousandhyehyang.mission.dto.MissionSearchRequest;
import com.spring_b.thousandhyehyang.mission.dto.UserMissionChallengeRequest;
import com.spring_b.thousandhyehyang.mission.dto.UserMissionPageResponse;
import com.spring_b.thousandhyehyang.mission.dto.UserMissionResponse;

public interface MissionService {

    MissionPageResponse getAvailableMissionsByRegion(MissionSearchRequest request);

    MissionPageResponse getMissionsByStoreId(Long storeId, Integer page, Integer size);

    UserMissionPageResponse getMyMissions(Long userId, Integer page, Integer size);

    UserMissionPageResponse getInProgressMissions(Long userId, Integer page, Integer size);

    UserMissionPageResponse getCompletedMissions(Long userId, Integer page, Integer size);

    MissionResponse createMission(Long storeId, Long ownerId, MissionCreateRequest request);

    UserMissionResponse challengeMission(Long missionId, UserMissionChallengeRequest request);

    UserMissionResponse completeMission(Long userId, Long missionId);
}

