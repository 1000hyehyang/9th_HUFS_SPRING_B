package com.spring_b.thousandhyehyang.store.repository;

import com.spring_b.thousandhyehyang.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreRepositoryCustom {

    /**
     * 가게 검색 (QueryDSL 사용)
     *
     * @param regions 지역 필터 (시/군/구) - 다중 선택 가능
     * @param searchKeyword 검색어 (공백 포함 시 각 단어의 합집합, 공백 없으면 전체 포함)
     * @param pageable 페이징 및 정렬 정보
     * @return Page<Store>
     */
    Page<Store> searchStores(List<String> regions, String searchKeyword, Pageable pageable);
}

