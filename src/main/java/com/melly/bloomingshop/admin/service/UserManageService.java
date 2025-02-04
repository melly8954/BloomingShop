package com.melly.bloomingshop.admin.service;

import com.melly.bloomingshop.domain.StatusType;
import com.melly.bloomingshop.domain.User;
import com.melly.bloomingshop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserManageService {
    private final UserRepository userRepository;

    // 모든 회원 정보 및 페이징 처리
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    // name 으로 회원정보 찾기
    public Page<User> getUserByName(String name, Pageable pageable) {
        return userRepository.findByName(name, pageable);
    }

    // 계정 상태 변경 비즈니스 로직
    public void updateUserStatus(Long id, StatusType status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        if (status == StatusType.DISABLED) { // 비활성화
            user.changeStatus(StatusType.DISABLED);
            user.changeDisabledDate(LocalDateTime.now());
        } else if (status == StatusType.ACTIVE) { // 복구 (DISABLED → ACTIVE, DELETED → ACTIVE)
            if (user.getStatus() == StatusType.DISABLED || user.getStatus() == StatusType.DELETED) {
                user.changeStatus(StatusType.ACTIVE);
                user.changeDeletedDate(null);
                user.changeDisabledDate(null);
            } else {
                throw new IllegalStateException("유저가 이미 활성 상태입니다.");
            }
        } else {
            throw new IllegalArgumentException("잘못된 상태 변경 요청입니다.");
        }
        userRepository.save(user);
    }

    // 계정 탈퇴 처리 비즈니스 로직
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        user.changeStatus(StatusType.DELETED);
        user.changeDeletedDate(LocalDateTime.now());
        userRepository.save(user);
    }
}
