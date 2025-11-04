package com.spring_b.thousandhyehyang.user.repository;

import com.spring_b.thousandhyehyang.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 마이페이지: 사용자 정보 조회 (이름, 이메일, 휴대폰 번호, 인증 여부)
     */
    @Query("SELECT u FROM User u " +
           "WHERE u.userId = :userId " +
           "AND u.deletedAt IS NULL")
    Optional<User> findUserForMyPage(@Param("userId") Long userId);
}
