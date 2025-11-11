package com.spring_b.thousandhyehyang.user.converter;

import com.spring_b.thousandhyehyang.user.dto.UserMyPageResponse;
import com.spring_b.thousandhyehyang.user.entity.User;

public final class UserConverter {

    private UserConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static UserMyPageResponse toMyPageResponse(User user, Integer currentPoints) {
        if (user == null) {
            return null;
        }

        return UserMyPageResponse.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .phoneVerified(user.isPhoneVerified())
                .currentPoints(currentPoints)
                .build();
    }
}

