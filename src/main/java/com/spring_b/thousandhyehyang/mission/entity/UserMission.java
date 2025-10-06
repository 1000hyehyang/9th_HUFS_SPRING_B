package com.spring_b.thousandhyehyang.mission.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import com.spring_b.thousandhyehyang.mission.enums.MissionStatus;
import com.spring_b.thousandhyehyang.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Entity
@Table(name = "user_mission",
    uniqueConstraints = @UniqueConstraint(name = "uk_user_mission", columnNames = {"user_id", "mission_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserMission extends BaseEntity {
    
    @EmbeddedId
    private UserMissionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_um_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("missionId")
    @JoinColumn(name = "mission_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_um_mission"))
    private Mission mission;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private MissionStatus status;

    private Instant completedAt;
}
