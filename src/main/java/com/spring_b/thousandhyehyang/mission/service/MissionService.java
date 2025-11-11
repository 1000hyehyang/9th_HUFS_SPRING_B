package com.spring_b.thousandhyehyang.mission.service;

import com.spring_b.thousandhyehyang.mission.dto.MissionPageResponse;
import com.spring_b.thousandhyehyang.mission.dto.MissionSearchRequest;
import com.spring_b.thousandhyehyang.mission.dto.UserMissionPageResponse;

public interface MissionService {

    MissionPageResponse getAvailableMissionsByRegion(MissionSearchRequest request);

    UserMissionPageResponse getMyMissions(Long userId, Integer page, Integer size);

    UserMissionPageResponse getCompletedMissions(Long userId, Integer page, Integer size);
}

