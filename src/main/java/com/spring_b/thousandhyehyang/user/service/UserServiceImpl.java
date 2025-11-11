package com.spring_b.thousandhyehyang.user.service;

import com.spring_b.thousandhyehyang.point.repository.PointTransactionRepository;
import com.spring_b.thousandhyehyang.user.converter.UserConverter;
import com.spring_b.thousandhyehyang.user.dto.UserMyPageResponse;
import com.spring_b.thousandhyehyang.user.entity.User;
import com.spring_b.thousandhyehyang.user.exception.UserErrorCode;
import com.spring_b.thousandhyehyang.user.exception.UserException;
import com.spring_b.thousandhyehyang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PointTransactionRepository pointTransactionRepository;

    @Override
    public UserMyPageResponse getMyPageInfo(Long userId) {
        log.info("마이페이지 정보 조회 요청 - userId: {}", userId);

        User user = userRepository.findUserForMyPage(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Integer currentPoints = pointTransactionRepository.findCurrentBalanceByUserId(userId);

        return UserConverter.toMyPageResponse(user, currentPoints);
    }
}

