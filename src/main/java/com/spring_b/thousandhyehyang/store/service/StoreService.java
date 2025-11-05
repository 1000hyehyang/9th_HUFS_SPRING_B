package com.spring_b.thousandhyehyang.store.service;

import com.spring_b.thousandhyehyang.review.repository.ReviewRepository;
import com.spring_b.thousandhyehyang.store.dto.StoreResponse;
import com.spring_b.thousandhyehyang.store.dto.StoreSearchRequest;
import com.spring_b.thousandhyehyang.store.entity.Store;
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
public class StoreService {

    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 가게 평균 평점 업데이트
     * 
     * @param storeId 가게 ID
     */
    @Transactional
    public void updateAverageRating(Long storeId) {
        
        Double averageRating = reviewRepository.calculateAverageRatingByStoreId(storeId);
        
        if (averageRating != null) {
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다: " + storeId));
            store.setAvgRating(averageRating);
            storeRepository.save(store);
            
            log.info("가게 평균 평점 업데이트 완료 - storeId: {}, averageRating: {}", storeId, averageRating);
        } else {
            log.warn("가게 평균 평점이 null입니다 - storeId: {}", storeId);
        }
    }

    /**
     * 가게 검색 (QueryDSL 사용)
     *
     * @param request 검색 요청 DTO (지역, 검색어, 정렬, 페이징)
     * @return Page<StoreResponse>
     */
    public Page<StoreResponse> searchStores(StoreSearchRequest request) {

        // 정렬 설정
        Sort sort = createSort(request.getSortBy(), request.getSortDirection());

        // 페이징 설정
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        // Repository 호출
        Page<Store> storePage = storeRepository.searchStores(
                request.getRegions(),
                request.getSearchKeyword(),
                pageable
        );

        // Entity를 DTO로 변환
        List<StoreResponse> storeResponses = storePage.getContent().stream()
                .map(StoreResponse::from)
                .collect(Collectors.toList());

        return new PageImpl<>(storeResponses, pageable, storePage.getTotalElements());
    }

    /**
     * 정렬 설정 생성
     *
     * 정렬 조건:
     * - "latest": 최신순 (createdAt DESC)
     * - "name": 이름순 (name ASC, 동일하면 createdAt DESC)
     *
     * @param sortBy 정렬 기준 ("latest", "name")
     * @param sortDirection 정렬 방향 ("ASC", "DESC") - name 정렬 시 무시됨
     * @return Sort 객체
     */
    private Sort createSort(String sortBy, String sortDirection) {
        // 기본값: 최신순
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        String normalizedSortBy = sortBy.trim().toLowerCase();

        switch (normalizedSortBy) {
            case "latest":
                // 최신순: createdAt DESC
                return Sort.by(Sort.Direction.DESC, "createdAt");

            case "name":
                // 이름순: name ASC, 동일하면 createdAt DESC
                return Sort.by(
                        Sort.Order.asc("name"),
                        Sort.Order.desc("createdAt")
                );

            default:
                return Sort.by(Sort.Direction.DESC, "createdAt");
        }
    }
}
