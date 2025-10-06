package com.spring_b.thousandhyehyang.review.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "review_image")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ReviewImage extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_rimg_review"))
    private Review review;

    @Column(nullable = false, length = 500)
    private String url;
}
