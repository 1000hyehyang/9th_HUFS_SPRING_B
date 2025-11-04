package com.spring_b.thousandhyehyang.user.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import com.spring_b.thousandhyehyang.user.enums.FoodCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "user_food_preference",
    uniqueConstraints = @UniqueConstraint(name = "uk_user_food", columnNames = {"user_id", "food_category"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFoodPreference that)) return false;

        return foodCategory == that.foodCategory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodCategory);
    }
}