package com.spring_b.thousandhyehyang.store.service;

import com.spring_b.thousandhyehyang.review.repository.ReviewRepository;
import com.spring_b.thousandhyehyang.store.converter.StoreConverter;
import com.spring_b.thousandhyehyang.store.dto.StoreResponse;
import com.spring_b.thousandhyehyang.store.dto.StoreSearchRequest;
import com.spring_b.thousandhyehyang.store.entity.Store;
import com.spring_b.thousandhyehyang.store.exception.StoreErrorCode;
import com.spring_b.thousandhyehyang.store.exception.StoreException;
import com.spring_b.thousandhyehyang.store.repository.StoreRepository;
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

