package com.spring_b.thousandhyehyang.review.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import com.spring_b.thousandhyehyang.user.entity.User;
import com.spring_b.thousandhyehyang.store.entity.Store;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review",
    uniqueConstraints = @UniqueConstraint(name = "uk_review_user_store", columnNames = {"user_id", "store_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Review extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_review_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_review_store"))
    private Store store;

    @Column(nullable = false)
    @DecimalMin(value = "0.0", message = "평점은 0점 이상이어야 합니다")
    @DecimalMax(value = "5.0", message = "평점은 5점 이하여야 합니다")
    private Double rating;

    @Column(nullable = false, length = 2000)
    private String content;

    // 1:N 관계 - ReviewImage
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ReviewImage> reviewImages = new ArrayList<>();

    // 1:N 관계 - ReviewReply
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ReviewReply> reviewReplies = new ArrayList<>();
}
