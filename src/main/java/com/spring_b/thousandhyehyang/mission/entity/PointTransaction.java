package com.spring_b.thousandhyehyang.mission.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import com.spring_b.thousandhyehyang.mission.enums.PointType;
import com.spring_b.thousandhyehyang.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "point_transaction")
@Check(constraints = "balance_after between 0 and 999999")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class PointTransaction extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_pt_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id",
        foreignKey = @ForeignKey(name = "fk_pt_mission"))
    private Mission mission;

    // 포인트 거래 유형
    // - EARN: 적립, USE: 사용(차감), ADJUST: 관리자 조정 등
    @Enumerated(EnumType.STRING)
    @Column(name = "point_type", nullable = false, length = 10)
    private PointType type;

    // 거래 금액 
    // - 적립(EARN, ADJUST+)일 때 양수, 사용(USE, ADJUST-)일 때 음수
    // - 절대값 상한은 999,999로 제한
    @Column(nullable = false)
    @Min(-999999)
    @Max(999999)
    private Integer amount;

    // 거래 처리 후 사용자 보유 포인트 잔액
    @Column(nullable = false)
    @Min(0)
    @Max(999999)
    private Integer balanceAfter;

    // 거래 발생 출처(코드)
    // 예) MISSION_CLEAR(미션 완료 보상), ADMIN_ADJUST(관리자 조정) 등
    @Column(length = 50)
    private String source;

    @Column(length = 255)
    private String description;
}
