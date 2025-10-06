package com.spring_b.thousandhyehyang.review.entity;

import com.spring_b.thousandhyehyang.global.entity.BaseEntity;
import com.spring_b.thousandhyehyang.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review_reply")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ReviewReply extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_rreply_review"))
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_rreply_user"))
    private User user; // 점주/일반/관리자 모두 가능

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to",
        foreignKey = @ForeignKey(name = "fk_rreply_parent"))
    private ReviewReply parent; // null이면 원댓글

    @Column(nullable = false, length = 1000)
    private String content;

    // 자기참조 - 대댓글들
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ReviewReply> childReplies = new ArrayList<>();
}
