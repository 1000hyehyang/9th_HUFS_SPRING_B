package com.spring_b.thousandhyehyang.user.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "notification_setting")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class NotificationSetting extends BaseEntity {
    
    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_notiset_user"))
    @MapsId
    private User user;

    @Column(nullable = false)
    @Builder.Default
    private boolean newEvent = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean reviewReply = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean inquiryReply = true;
}
