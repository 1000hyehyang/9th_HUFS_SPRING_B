package com.spring_b.thousandhyehyang.mission.controller;

import com.spring_b.thousandhyehyang.global.apiPayload.ApiResponse;
import com.spring_b.thousandhyehyang.global.apiPayload.code.GeneralSuccessCode;
import com.spring_b.thousandhyehyang.mission.dto.UserMissionChallengeRequest;
import com.spring_b.thousandhyehyang.mission.dto.UserMissionResponse;
import com.spring_b.thousandhyehyang.mission.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
@Tag(name = "Mission", description = "미션 관련 API")
public class MissionController {

    private final MissionService missionService;

    @Operation(summary = "미션 도전하기", description = "가게의 미션을 도전 중인 미션에 추가합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "미션 도전 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (Validation 실패)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "미션 또는 사용자를 찾을 수 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 도전 중인 미션")
    })
    @PostMapping("/{missionId}/challenge")
    public ResponseEntity<ApiResponse<UserMissionResponse>> challengeMission(
            @Parameter(description = "미션 ID", required = true, example = "1")
            @PathVariable Long missionId,
            @Valid @RequestBody UserMissionChallengeRequest request) {

        UserMissionResponse response = missionService.challengeMission(missionId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(GeneralSuccessCode.OK, response));
    }
}

