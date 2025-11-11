package com.spring_b.thousandhyehyang.store.controller;

import com.spring_b.thousandhyehyang.global.apiPayload.ApiResponse;
import com.spring_b.thousandhyehyang.global.apiPayload.code.GeneralSuccessCode;
import com.spring_b.thousandhyehyang.store.dto.StoreResponse;
import com.spring_b.thousandhyehyang.store.dto.StoreSearchRequest;
import com.spring_b.thousandhyehyang.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /**
     * 가게 검색 API (QueryDSL 사용)
     *
     * 필터링 조건:
     * - regions: 지역 필터 (시/군/구) - 다중 선택 가능
     * - searchKeyword: 가게명 검색어
     *   * 공백 포함: 각 단어가 포함된 가게의 합집합 조회
     *   * 공백 없음: 검색어 전체가 포함된 가게만 조회
     *
     * 정렬 조건:
     * - sortBy: "latest" (최신순) 또는 "name" (이름순)
     *
     * 페이징:
     * - page: 페이지 번호 (기본값: 0)
     * - size: 페이지 크기 (기본값: 10)
     *
     * @param request 검색 요청 DTO
     * @return Page<StoreResponse>
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<StoreResponse>>> searchStores(
            @Valid @ModelAttribute StoreSearchRequest request) {

        Page<StoreResponse> stores = storeService.searchStores(request);

        return ResponseEntity
                .status(GeneralSuccessCode.OK.getStatus())
                .body(ApiResponse.onSuccess(GeneralSuccessCode.OK, stores));
    }
}
