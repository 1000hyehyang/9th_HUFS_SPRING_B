package com.spring_b.thousandhyehyang.user.dto;

import com.spring_b.thousandhyehyang.user.enums.FoodCategory;
import com.spring_b.thousandhyehyang.user.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignupResponse {

    private Long userId;
    private String email;
    private String nickname;
    private Gender gender;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
    private Boolean phoneVerified;
    private List<FoodCategory> preferredFoods;
}

