package com.spring_b.thousandhyehyang.inquiry.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import com.spring_b.thousandhyehyang.inquiry.enums.InquiryType;
import com.spring_b.thousandhyehyang.inquiry.enums.InquiryStatus;
import com.spring_b.thousandhyehyang.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inquiry")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Inquiry extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_inquiry_user"))
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_type", nullable = false, length = 20)
    private InquiryType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_status", nullable = false, length = 15)
    @Builder.Default
    private InquiryStatus status = InquiryStatus.PENDING;

    @Column(nullable = false, length = 2000)
    private String content;

    // 1:N 관계 - InquiryImage
    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<InquiryImage> inquiryImages = new ArrayList<>();

    // 1:N 관계 - InquiryReply
    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<InquiryReply> inquiryReplies = new ArrayList<>();
}
