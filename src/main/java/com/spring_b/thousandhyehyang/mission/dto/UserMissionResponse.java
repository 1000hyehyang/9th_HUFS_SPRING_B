package com.spring_b.thousandhyehyang.mission.dto;

import com.spring_b.thousandhyehyang.mission.entity.UserMission;
import com.spring_b.thousandhyehyang.mission.enums.MissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMissionResponse {

    private Long userId;
    private Long missionId;
    private String missionTitle;
    private String missionDescription;
    private Integer rewardPoint;
    private Long storeId;
    private String storeName;
    private MissionStatus status;
    private Instant completedAt;
    private Instant createdAt;

    public static UserMissionResponse from(UserMission userMission) {
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

