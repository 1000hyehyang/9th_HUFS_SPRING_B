package com.spring_b.thousandhyehyang.store.service;

import com.spring_b.thousandhyehyang.store.dto.StoreResponse;
import com.spring_b.thousandhyehyang.store.dto.StoreSearchRequest;
import org.springframework.data.domain.Page;

public interface StoreService {

    void updateAverageRating(Long storeId);

    Page<StoreResponse> searchStores(StoreSearchRequest request);
}
