package com.spring_b.thousandhyehyang.store.dto;

import com.spring_b.thousandhyehyang.store.entity.Store;
import com.spring_b.thousandhyehyang.user.enums.FoodCategory;
import com.spring_b.thousandhyehyang.store.enums.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public static StoreResponse from(Store store) {
        if (store == null) {
            return null;
        }

        // 삭제되지 않은 StoreImage만 포함
        List<String> imageUrls = store.getStoreImages().stream()
                .filter(img -> img.getDeletedAt() == null)
                .map(img -> img.getUrl())
                .collect(Collectors.toList());

        return StoreResponse.builder()
                .storeId(store.getStoreId())
                .name(store.getName())
                .category(store.getCategory())
                .address(store.getAddress())
                .sido(store.getSido())
                .sigungu(store.getSigungu())
                .dong(store.getDong())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .status(store.getStatus())
                .avgRating(store.getAvgRating())
                .description(store.getDescription())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .contactPhone(store.getContactPhone())
                .contactEmail(store.getContactEmail())
                .imageUrls(imageUrls)
                .createdAt(store.getCreatedAt())
                .updatedAt(store.getUpdatedAt())
                .build();
    }
}
