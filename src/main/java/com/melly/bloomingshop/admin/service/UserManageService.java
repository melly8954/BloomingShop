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

    // 관리자가 계정을 비활성화
    @Transactional
    public void softDisabledUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        // 계정을 비활성화 상태로 변경
        user.changeStatus(StatusType.DISABLED); // 상태를 INACTIVE로 변경
        user.changeDisabledDate(LocalDateTime.now()); // 비활성화 날짜를 현재 시간으로 설정
        userRepository.save(user); // 변경 사항 저장
    }

    // 사용자 계정 복구
    @Transactional
    public void undoUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        // 계정이 비활성화(인 inactive) 또는 삭제(deleted) 상태인 경우 복구
        if (user.getStatus() == StatusType.DISABLED || user.getStatus() == StatusType.DELETED) {
            user.changeStatus(StatusType.ACTIVE); // 상태를 ACTIVE로 변경
            user.changeDeletedDate(null); // 비활성화 날짜 및 삭제 날짜 초기화
            user.changeDisabledDate(null); // 비활성화 날짜 및 삭제 날짜 초기화
            userRepository.save(user); // 변경 사항 저장
        } else {
            throw new IllegalStateException("User is already active and cannot be undone.");
        }
    }
}
