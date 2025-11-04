package com.spring_b.thousandhyehyang.store.service;

import com.spring_b.thousandhyehyang.review.repository.ReviewRepository;
import com.spring_b.thousandhyehyang.store.entity.Store;
import com.spring_b.thousandhyehyang.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
