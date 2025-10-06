package com.spring_b.thousandhyehyang.user.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import com.spring_b.thousandhyehyang.global.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_notification_user"))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 20)
    private NotificationType type;

    @Column(nullable = false, length = 200)
    private String message;

    @Column(nullable = false)
    @Builder.Default
    private boolean isRead = false;
}
