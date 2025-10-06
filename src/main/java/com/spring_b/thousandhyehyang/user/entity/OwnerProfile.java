package com.spring_b.thousandhyehyang.user.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import com.spring_b.thousandhyehyang.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "owner_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class OwnerProfile extends BaseEntity {
    
    @Id
    private Long ownerId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", foreignKey = @ForeignKey(name = "fk_owner_profile_user"))
    private User user;

    @Column(nullable = false, length = 20)
    private String businessNumber;

    @Column(nullable = false, length = 100)
    private String businessName; // 사업자등록증상 상호명

    @Column(nullable = false, length = 50)
    private String representativeName;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 20)
    private String businessPhoneNumber; // 사업자용 전화번호

    // 1:N 관계 - Store
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Store> stores = new ArrayList<>();
}
