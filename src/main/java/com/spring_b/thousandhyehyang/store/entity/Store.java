package com.spring_b.thousandhyehyang.store.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import com.spring_b.thousandhyehyang.user.entity.OwnerProfile;
import com.spring_b.thousandhyehyang.user.enums.FoodCategory;
import com.spring_b.thousandhyehyang.store.enums.StoreStatus;
import com.spring_b.thousandhyehyang.mission.entity.Mission;
import com.spring_b.thousandhyehyang.review.entity.Review;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "store")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Store extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_store_owner"))
    private OwnerProfile owner; // OwnerProfile 기반

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FoodCategory category;

    // 카카오 API로 정규화된 주소 정보
    @Column(nullable = false, length = 255)
    private String address; // 전체 주소

    @Column(name = "sido", nullable = false, length = 30)
    private String sido; // 시/도 (카카오 API에서 추출)

    @Column(name = "sigungu", nullable = false, length = 30)
    private String sigungu; // 시/군/구 (카카오 API에서 추출)

    @Column(name = "dong", length = 30)
    private String dong; // 동/읍/면 (카카오 API에서 추출)

    // 지도 핀 표시용 좌표 (카카오 API로 자동 생성)
    @Column(nullable = false)
    private Double latitude; // 위도

    @Column(nullable = false)
    private Double longitude; // 경도

    @Enumerated(EnumType.STRING)
    @Column(name = "store_status", nullable = false, length = 25)
    @Builder.Default
    private StoreStatus status = StoreStatus.OPEN;

    @Column(nullable = false)
    @DecimalMin(value = "0.0", message = "평균 평점은 0점 이상이어야 합니다")
    @DecimalMax(value = "5.0", message = "평균 평점은 5점 이하여야 합니다")
    @Builder.Default
    private Double avgRating = 0.0;

    @Column(length = 1000)
    private String description;

    @Column(name = "open_time")
    private LocalTime openTime; 

    @Column(name = "close_time")
    private LocalTime closeTime;

    // 연락처 정보
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    // 1:N 관계 - StoreImage
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<StoreImage> storeImages = new ArrayList<>();

    // 1:N 관계 - Mission
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Mission> missions = new ArrayList<>();

    // 1:N 관계 - Review
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
}
