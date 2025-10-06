package com.spring_b.thousandhyehyang.user.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import com.spring_b.thousandhyehyang.user.enums.FoodCategory;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "user_food_preference",
    uniqueConstraints = @UniqueConstraint(name = "uk_user_food", columnNames = {"user_id", "food_category"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserFoodPreference extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_ufp_user"))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "food_category", nullable = false, length = 20)
    private FoodCategory foodCategory;
}