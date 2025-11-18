package com.spring_b.thousandhyehyang.user.service;

import com.spring_b.thousandhyehyang.global.enums.UserRole;
import com.spring_b.thousandhyehyang.point.repository.PointTransactionRepository;
import com.spring_b.thousandhyehyang.user.converter.UserConverter;
import com.spring_b.thousandhyehyang.user.dto.UserMyPageResponse;
import com.spring_b.thousandhyehyang.user.dto.UserSignupRequest;
import com.spring_b.thousandhyehyang.user.dto.UserSignupResponse;
import com.spring_b.thousandhyehyang.user.entity.User;
import com.spring_b.thousandhyehyang.user.entity.UserFoodPreference;
import com.spring_b.thousandhyehyang.user.exception.UserErrorCode;
import com.spring_b.thousandhyehyang.user.exception.UserException;
import com.spring_b.thousandhyehyang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    @Transactional
    public UserSignupResponse signup(UserSignupRequest request) {
        log.info("회원가입 요청 - email: {}, nickname: {}", request.getEmail(), request.getNickname());

        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 닉네임 중복 체크
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new UserException(UserErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        // User 엔티티 생성 (이메일은 소문자로 변환하여 저장)
        User user = User.builder()
                .email(request.getEmail().toLowerCase().trim())
                .nickname(request.getNickname().trim())
                .role(UserRole.USER)
                .gender(request.getGender())
                .birthDate(request.getBirthDate())
                .address(request.getAddress() != null ? request.getAddress().trim() : null)
                .phoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber().trim() : null)
                .phoneVerified(false)
                .build();

        // 선호 음식 설정 (stream 사용)
        if (request.getPreferredFoods() != null && !request.getPreferredFoods().isEmpty()) {
            Set<UserFoodPreference> foodPreferences = request.getPreferredFoods().stream()
                    .distinct() // 중복 제거
                    .map(foodCategory -> UserFoodPreference.builder()
                            .user(user)
                            .foodCategory(foodCategory)
                            .build())
                    .collect(Collectors.toSet());
            user.setFoodPreferences(foodPreferences);
        }

        // User 저장 (cascade로 UserFoodPreference도 함께 저장됨)
        User savedUser = userRepository.save(user);

        log.info("회원가입 완료 - userId: {}, email: {}", savedUser.getUserId(), savedUser.getEmail());

        return UserConverter.toSignupResponse(savedUser);
    }
}

