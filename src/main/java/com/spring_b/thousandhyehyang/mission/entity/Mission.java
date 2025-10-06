package com.spring_b.thousandhyehyang.mission.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import com.spring_b.thousandhyehyang.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Mission extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long missionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_mission_store"))
    private Store store;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer rewardPoint;

    // 1:N 관계 - UserMission
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserMission> userMissions = new ArrayList<>();

    // 1:N 관계 - PointTransaction
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PointTransaction> pointTransactions = new ArrayList<>();
}
