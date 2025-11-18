package com.spring_b.thousandhyehyang.user.controller;

import com.spring_b.thousandhyehyang.global.apiPayload.ApiResponse;
import com.spring_b.thousandhyehyang.global.apiPayload.code.GeneralSuccessCode;
import com.spring_b.thousandhyehyang.user.dto.UserSignupRequest;
import com.spring_b.thousandhyehyang.user.dto.UserSignupResponse;
import com.spring_b.thousandhyehyang.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 API
     *
     * @param request 회원가입 요청 DTO
     * @return UserSignupResponse
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserSignupResponse>> signup(
            @Valid @RequestBody UserSignupRequest request) {

        UserSignupResponse response = userService.signup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(GeneralSuccessCode.OK, response));
    }
}

