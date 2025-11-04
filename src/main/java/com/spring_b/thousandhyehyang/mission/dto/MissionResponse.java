package com.spring_b.thousandhyehyang.mission.dto;

import com.spring_b.thousandhyehyang.mission.entity.Mission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionResponse {

    private Long missionId;
    private String title;
    private String description;
    private Integer rewardPoint;
    private Instant createdAt;
    private Instant updatedAt;

    // Store 정보
    private Long storeId;
    private String storeName;
    private String storeAddress;
    private String storeSido;
    private String storeSigungu;

    public static MissionResponse from(Mission mission) {
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
}

