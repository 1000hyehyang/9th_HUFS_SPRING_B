package com.spring_b.thousandhyehyang.store.converter;

import com.spring_b.thousandhyehyang.store.dto.StoreResponse;
import com.spring_b.thousandhyehyang.store.entity.Store;
import java.util.List;
import java.util.stream.Collectors;

public final class StoreConverter {

    private StoreConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static StoreResponse toResponse(Store store) {
        if (store == null) {
            return null;
        }

        List<String> imageUrls = store.getStoreImages().stream()
                .filter(image -> image.getDeletedAt() == null)
                .map(image -> image.getUrl())
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

