package com.spring_b.thousandhyehyang.mission.converter;

import com.spring_b.thousandhyehyang.mission.dto.MissionResponse;
import com.spring_b.thousandhyehyang.mission.dto.UserMissionResponse;
import com.spring_b.thousandhyehyang.mission.entity.Mission;
import com.spring_b.thousandhyehyang.mission.entity.UserMission;

public final class MissionConverter {

    private MissionConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static MissionResponse toMissionResponse(Mission mission) {
        if (mission == null) {
            return null;
        }

        return MissionResponse.builder()
                .missionId(mission.getMissionId())
                .title(mission.getTitle())
                .description(mission.getDescription())
                .rewardPoint(mission.getRewardPoint())
                .createdAt(mission.getCreatedAt())
                .updatedAt(mission.getUpdatedAt())
                .storeId(mission.getStore().getStoreId())
                .storeName(mission.getStore().getName())
                .storeAddress(mission.getStore().getAddress())
                .storeSido(mission.getStore().getSido())
                .storeSigungu(mission.getStore().getSigungu())
                .build();
    }

    public static UserMissionResponse toUserMissionResponse(UserMission userMission) {
        if (userMission == null) {
            return null;
        }

        return UserMissionResponse.builder()
                .userId(userMission.getUser().getUserId())
                .missionId(userMission.getMission().getMissionId())
                .missionTitle(userMission.getMission().getTitle())
                .missionDescription(userMission.getMission().getDescription())
                .rewardPoint(userMission.getMission().getRewardPoint())
                .storeId(userMission.getMission().getStore().getStoreId())
                .storeName(userMission.getMission().getStore().getName())
                .status(userMission.getStatus())
                .completedAt(userMission.getCompletedAt())
                .createdAt(userMission.getCreatedAt())
                .build();
    }
}

