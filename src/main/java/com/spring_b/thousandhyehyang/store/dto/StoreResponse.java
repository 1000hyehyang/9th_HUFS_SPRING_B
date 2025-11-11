package com.spring_b.thousandhyehyang.store.dto;

import com.spring_b.thousandhyehyang.store.enums.StoreStatus;
import com.spring_b.thousandhyehyang.user.enums.FoodCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponse {

    private Long storeId;
    private String name;
    private FoodCategory category;
    private String address;
    private String sido;
    private String sigungu;
    private String dong;
    private Double latitude;
    private Double longitude;
    private StoreStatus status;
    private Double avgRating;
    private String description;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String contactPhone;
    private String contactEmail;
    private List<String> imageUrls;
    private Instant createdAt;
    private Instant updatedAt;
}
