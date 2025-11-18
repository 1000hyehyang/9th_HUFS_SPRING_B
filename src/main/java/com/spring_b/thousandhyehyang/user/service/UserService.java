package com.spring_b.thousandhyehyang.user.service;

import com.spring_b.thousandhyehyang.user.dto.UserMyPageResponse;
import com.spring_b.thousandhyehyang.user.dto.UserSignupRequest;
import com.spring_b.thousandhyehyang.user.dto.UserSignupResponse;

public interface UserService {

    UserMyPageResponse getMyPageInfo(Long userId);

    UserSignupResponse signup(UserSignupRequest request);
}
