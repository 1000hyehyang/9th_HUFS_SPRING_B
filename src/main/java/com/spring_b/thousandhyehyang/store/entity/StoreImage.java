package com.spring_b.thousandhyehyang.store.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "store_image",
    indexes = @Index(name = "idx_store_image_store", columnList = "store_id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class StoreImage extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_store_image_store"))
    private Store store;

    @Column(nullable = false, length = 500)
    private String url;
}
