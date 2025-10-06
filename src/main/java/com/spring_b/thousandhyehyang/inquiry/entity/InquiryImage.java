package com.spring_b.thousandhyehyang.inquiry.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "inquiry_image")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class InquiryImage extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_iimg_inquiry"))
    private Inquiry inquiry;

    @Column(nullable = false, length = 500)
    private String url;
}
