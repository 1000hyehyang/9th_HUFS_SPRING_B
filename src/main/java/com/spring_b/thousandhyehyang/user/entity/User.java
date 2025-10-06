package com.spring_b.thousandhyehyang.user.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import com.spring_b.thousandhyehyang.global.enums.UserRole;
import com.spring_b.thousandhyehyang.user.enums.Gender;
import com.spring_b.thousandhyehyang.mission.entity.UserMission;
import com.spring_b.thousandhyehyang.mission.entity.PointTransaction;
import com.spring_b.thousandhyehyang.review.entity.Review;
import com.spring_b.thousandhyehyang.review.entity.ReviewReply;
import com.spring_b.thousandhyehyang.inquiry.entity.Inquiry;
import com.spring_b.thousandhyehyang.inquiry.entity.InquiryReply;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_users_social", columnNames = {"provider", "provider_id"})
    })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String email; // 저장 시 자동으로 소문자 변환됨

    @Column(nullable = false, length = 50)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, length = 10)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    @Builder.Default
    private Gender gender = Gender.NONE;

    private LocalDate birthDate;

    @Column(length = 255)
    private String address;

    @Column(length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    @Builder.Default
    private boolean phoneVerified = false;

    @Column(name = "provider", length = 30)
    private String provider; // 소셜 로그인 제공자

    @Column(name = "provider_id", length = 120)
    private String providerId; // 소셜 로그인 ID

    // 식별 1:1 관계 - OwnerProfile
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private OwnerProfile ownerProfile;

    // 1:1 관계 - UserConsent
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserConsent userConsent;

    // 1:1 관계 - NotificationSetting
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private NotificationSetting notificationSetting;

    // 1:N 관계 - Notification
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    // 1:N 관계 - PointTransaction (포인트 내역은 보존)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<PointTransaction> pointTransactions = new ArrayList<>();

    // 1:N 관계 - Review (리뷰는 보존, 가게 평점에 영향)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    // 1:N 관계 - ReviewReply (댓글은 보존)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<ReviewReply> reviewReplies = new ArrayList<>();

    // 1:N 관계 - Inquiry (문의는 보존, 고객 지원용)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Inquiry> inquiries = new ArrayList<>();

    // 1:N 관계 - InquiryReply (문의 답변은 보존)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<InquiryReply> inquiryReplies = new ArrayList<>();

    // N:M 관계 - UserMission (미션 참여 기록은 보존)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserMission> userMissions = new ArrayList<>();

    // N:M 관계 - UserFoodPreference (선호도는 삭제해도 됨)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserFoodPreference> foodPreferences = new ArrayList<>();
}
