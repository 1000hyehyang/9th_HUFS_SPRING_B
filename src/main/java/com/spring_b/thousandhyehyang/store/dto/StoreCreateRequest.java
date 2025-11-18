package com.spring_b.thousandhyehyang.store.dto;

import com.spring_b.thousandhyehyang.user.enums.FoodCategory;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreCreateRequest {

    @NotBlank(message = "가게명은 필수입니다")
    @Size(max = 100, message = "가게명은 100자 이하여야 합니다")
    private String name;

    @NotNull(message = "카테고리는 필수입니다")
    private FoodCategory category;

    @NotBlank(message = "주소는 필수입니다")
    @Size(max = 255, message = "주소는 255자 이하여야 합니다")
    private String address;

    @NotBlank(message = "시/도는 필수입니다")
    @Size(max = 30, message = "시/도는 30자 이하여야 합니다")
    private String sido;

    @NotBlank(message = "시/군/구는 필수입니다")
    @Size(max = 30, message = "시/군/구는 30자 이하여야 합니다")
    private String sigungu;

    @Size(max = 30, message = "동/읍/면은 30자 이하여야 합니다")
    private String dong;

    @NotNull(message = "위도는 필수입니다")
    @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다")
    @DecimalMax(value = "90.0", message = "위도는 90 이하여야 합니다")
    private Double latitude;

    @NotNull(message = "경도는 필수입니다")
    @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다")
    @DecimalMax(value = "180.0", message = "경도는 180 이하여야 합니다")
    private Double longitude;

    @Size(max = 1000, message = "설명은 1000자 이하여야 합니다")
    private String description;

    private LocalTime openTime;

    private LocalTime closeTime;

    @Pattern(regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$", message = "올바른 연락처 형식이 아닙니다")
    @Size(max = 20, message = "연락처는 20자 이하여야 합니다")
    private String contactPhone;

    @Email(message = "올바른 이메일 형식이 아닙니다")
    @Size(max = 100, message = "이메일은 100자 이하여야 합니다")
    private String contactEmail;

    @Size(max = 10, message = "이미지는 최대 10개까지 첨부할 수 있습니다")
    private List<String> imageUrls;
}

