package com.spring_b.thousandhyehyang.review.repository;

import com.spring_b.thousandhyehyang.review.entity.ReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Long> {
}

