package com.spring_b.thousandhyehyang.user.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;
import java.time.Instant;

@Entity
@Table(name = "user_consent")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserConsent extends BaseEntity {
    
    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_pref_user"))
    @MapsId
    private User user;

    // 선택 약관 동의 여부
    @Column(name = "location_consent", nullable = false)
    @Builder.Default
    private boolean locationConsent = false;

    @Column(name = "location_consent_at")
    private Instant locationConsentAt;

    @Column(name = "marketing_consent", nullable = false)
    @Builder.Default
    private boolean marketingConsent = false;

    @Column(name = "marketing_consent_at")
    private Instant marketingConsentAt;
}
