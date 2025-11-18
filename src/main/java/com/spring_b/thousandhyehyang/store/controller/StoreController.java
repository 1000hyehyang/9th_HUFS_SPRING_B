package com.spring_b.thousandhyehyang.store.controller;

import com.spring_b.thousandhyehyang.global.apiPayload.ApiResponse;
import com.spring_b.thousandhyehyang.global.apiPayload.code.GeneralSuccessCode;
import com.spring_b.thousandhyehyang.mission.dto.MissionCreateRequest;
import com.spring_b.thousandhyehyang.mission.dto.MissionResponse;
import com.spring_b.thousandhyehyang.mission.service.MissionService;
import com.spring_b.thousandhyehyang.store.dto.StoreCreateRequest;
import com.spring_b.thousandhyehyang.store.dto.StoreResponse;
import com.spring_b.thousandhyehyang.store.dto.StoreSearchRequest;
import com.spring_b.thousandhyehyang.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Tag(name = "Store", description = "가게 관련 API")
public class StoreController {

    private final StoreService storeService;
    private final MissionService missionService;

    @Operation(summary = "가게 추가", description = "특정 지역에 새로운 가게를 등록합니다. (주인만 가능)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "가게 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (Validation 실패)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음 (주인이 아님)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사장님 정보를 찾을 수 없음")
    })
    @PostMapping("/{ownerId}")
    public ResponseEntity<ApiResponse<StoreResponse>> createStore(
            @Parameter(description = "주인(Owner) 사용자 ID", required = true, example = "1")
            @PathVariable Long ownerId,
            @Valid @RequestBody StoreCreateRequest request) {

        StoreResponse response = storeService.createStore(ownerId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(GeneralSuccessCode.OK, response));
    }

    @Operation(summary = "가게에 미션 추가", description = "특정 가게에 새로운 미션을 등록합니다. (해당 가게를 소유한 주인만 가능)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "미션 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (Validation 실패)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음 (가게 주인이 아님)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "가게를 찾을 수 없음")
    })
    @PostMapping("/{storeId}/missions/{ownerId}")
    public ResponseEntity<ApiResponse<MissionResponse>> createMission(
            @Parameter(description = "가게 ID", required = true, example = "1")
            @PathVariable Long storeId,
            @Parameter(description = "주인(Owner) 사용자 ID", required = true, example = "1")
            @PathVariable Long ownerId,
            @Valid @RequestBody MissionCreateRequest request) {

        MissionResponse response = missionService.createMission(storeId, ownerId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(GeneralSuccessCode.OK, response));
    }

    @Operation(summary = "가게 검색", description = "지역, 가게명으로 가게를 검색합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "검색 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (Validation 실패)")
    })
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<StoreResponse>>> searchStores(
            @Valid @ModelAttribute StoreSearchRequest request) {

        Page<StoreResponse> stores = storeService.searchStores(request);

        return ResponseEntity
                .status(GeneralSuccessCode.OK.getStatus())
                .body(ApiResponse.onSuccess(GeneralSuccessCode.OK, stores));
    }
}

