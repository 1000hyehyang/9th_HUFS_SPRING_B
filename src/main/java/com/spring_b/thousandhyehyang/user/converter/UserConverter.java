package com.spring_b.thousandhyehyang.user.converter;

import com.spring_b.thousandhyehyang.user.dto.UserMyPageResponse;
import com.spring_b.thousandhyehyang.user.dto.UserSignupResponse;
import com.spring_b.thousandhyehyang.user.entity.User;
import com.spring_b.thousandhyehyang.user.entity.UserFoodPreference;

import java.util.List;
import java.util.stream.Collectors;

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

    public static UserSignupResponse toSignupResponse(User user) {
        if (user == null) {
            return null;
        }

        List<com.spring_b.thousandhyehyang.user.enums.FoodCategory> preferredFoods = null;
        if (user.getFoodPreferences() != null && !user.getFoodPreferences().isEmpty()) {
            preferredFoods = user.getFoodPreferences().stream()
                    .map(UserFoodPreference::getFoodCategory)
                    .collect(Collectors.toList());
        }

        return UserSignupResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .phoneVerified(user.isPhoneVerified())
                .preferredFoods(preferredFoods)
                .build();
    }
}

