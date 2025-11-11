package com.spring_b.thousandhyehyang.mission.dto;

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
}

