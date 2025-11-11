package com.spring_b.thousandhyehyang.mission.dto;

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
}

