package com.spring_b.thousandhyehyang.user.dto;

import com.spring_b.thousandhyehyang.global.validation.ValidAge;
import com.spring_b.thousandhyehyang.user.enums.FoodCategory;
import com.spring_b.thousandhyehyang.user.enums.Gender;
import jakarta.validation.constraints.*;
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
public class UserSignupRequest {

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @Size(max = 100, message = "이메일은 100자 이하여야 합니다")
    private String email;

    @NotBlank(message = "닉네임은 필수입니다")
    @Size(min = 2, max = 50, message = "닉네임은 2자 이상 50자 이하여야 합니다")
    private String nickname;

    @NotNull(message = "성별은 필수입니다")
    private Gender gender;

    @Past(message = "올바른 생년월일을 선택해주세요")
    @ValidAge(message = "만 14세 이상 120세 이하만 가입 가능합니다")
    private LocalDate birthDate;

    @Size(max = 255, message = "주소는 255자 이하여야 합니다")
    private String address;

    @Pattern(regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$", message = "올바른 휴대폰 번호 형식이 아닙니다")
    @Size(max = 20, message = "휴대폰 번호는 20자 이하여야 합니다")
    private String phoneNumber;

    @NotEmpty(message = "선호하는 음식은 최소 1개 이상 선택해야 합니다")
    @Size(max = 15, message = "선호하는 음식은 최대 15개까지 선택할 수 있습니다")
    private List<FoodCategory> preferredFoods;
}

