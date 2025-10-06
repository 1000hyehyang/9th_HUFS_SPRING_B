package com.spring_b.thousandhyehyang.inquiry.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import com.spring_b.thousandhyehyang.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "inquiry_reply")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class InquiryReply extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_ireply_inquiry"))
    private Inquiry inquiry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_ireply_user"))
    private User user;

    @Column(nullable = false, length = 2000)
    private String content;
}
