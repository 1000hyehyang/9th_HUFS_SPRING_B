package com.spring_b.thousandhyehyang.user.service;

import com.spring_b.thousandhyehyang.user.dto.UserMyPageResponse;

public interface UserService {

    UserMyPageResponse getMyPageInfo(Long userId);
}
