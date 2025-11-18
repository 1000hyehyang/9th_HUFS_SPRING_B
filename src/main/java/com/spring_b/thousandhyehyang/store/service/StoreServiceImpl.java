package com.spring_b.thousandhyehyang.store.service;

import com.spring_b.thousandhyehyang.review.repository.ReviewRepository;
import com.spring_b.thousandhyehyang.store.converter.StoreConverter;
import com.spring_b.thousandhyehyang.store.dto.StoreCreateRequest;
import com.spring_b.thousandhyehyang.store.dto.StoreResponse;
import com.spring_b.thousandhyehyang.store.dto.StoreSearchRequest;
import com.spring_b.thousandhyehyang.store.entity.Store;
import com.spring_b.thousandhyehyang.store.entity.StoreImage;
import com.spring_b.thousandhyehyang.store.exception.StoreErrorCode;
import com.spring_b.thousandhyehyang.store.exception.StoreException;
import com.spring_b.thousandhyehyang.store.repository.StoreRepository;
import com.spring_b.thousandhyehyang.user.entity.OwnerProfile;
import com.spring_b.thousandhyehyang.user.entity.User;
import com.spring_b.thousandhyehyang.user.exception.UserErrorCode;
import com.spring_b.thousandhyehyang.user.exception.UserException;
import com.spring_b.thousandhyehyang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void updateAverageRating(Long storeId) {
        Double averageRating = reviewRepository.calculateAverageRatingByStoreId(storeId);

        if (averageRating != null) {
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));
            store.setAvgRating(averageRating);
            storeRepository.save(store);

            log.info("가게 평균 평점 업데이트 완료 - storeId: {}, averageRating: {}", storeId, averageRating);
        } else {
            log.warn("가게 평균 평점이 null입니다 - storeId: {}", storeId);
        }
    }

    @Override
    @Transactional
    public StoreResponse createStore(Long ownerId, StoreCreateRequest request) {
        // User 조회
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 권한 검증: OWNER 역할인지 확인
        if (user.getRole() != com.spring_b.thousandhyehyang.global.enums.UserRole.OWNER) {
            throw new StoreException(StoreErrorCode.OWNER_NOT_FOUND);
        }

        // OwnerProfile 조회
        OwnerProfile ownerProfile = user.getOwnerProfile();
        if (ownerProfile == null) {
            throw new StoreException(StoreErrorCode.OWNER_PROFILE_NOT_FOUND);
        }

        // Store 엔티티 생성
        Store store = Store.builder()
                .owner(ownerProfile)
                .name(request.getName().trim())
                .category(request.getCategory())
                .address(request.getAddress().trim())
                .sido(request.getSido().trim())
                .sigungu(request.getSigungu().trim())
                .dong(request.getDong() != null ? request.getDong().trim() : null)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .description(request.getDescription() != null ? request.getDescription().trim() : null)
                .openTime(request.getOpenTime())
                .closeTime(request.getCloseTime())
                .contactPhone(request.getContactPhone() != null ? request.getContactPhone().trim() : null)
                .contactEmail(request.getContactEmail() != null ? request.getContactEmail().trim() : null)
                .build();

        // StoreImage 설정 (stream 사용)
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<StoreImage> storeImages = request.getImageUrls().stream()
                    .distinct() // 중복 제거
                    .map(imageUrl -> StoreImage.builder()
                            .store(store)
                            .url(imageUrl)
                            .build())
                    .collect(Collectors.toList());
            store.setStoreImages(storeImages);
        }

        // Store 저장 (cascade로 StoreImage도 함께 저장됨)
        Store savedStore = storeRepository.save(store);

        return StoreConverter.toResponse(savedStore);
    }

    @Override
    public Page<StoreResponse> searchStores(StoreSearchRequest request) {
        Sort sort = createSort(request.getSortBy(), request.getSortDirection());

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<Store> storePage = storeRepository.searchStores(
                request.getRegions(),
                request.getSearchKeyword(),
                pageable
        );

        List<StoreResponse> storeResponses = storePage.getContent().stream()
                .map(StoreConverter::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(storeResponses, pageable, storePage.getTotalElements());
    }

    private Sort createSort(String sortBy, String sortDirection) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        String normalizedSortBy = sortBy.trim().toLowerCase();

        return switch (normalizedSortBy) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "name" -> Sort.by(
                    Sort.Order.asc("name"),
                    Sort.Order.desc("createdAt")
            );
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
    }
}

