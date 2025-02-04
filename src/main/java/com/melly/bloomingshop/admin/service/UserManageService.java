package com.melly.bloomingshop.admin.service;

import com.melly.bloomingshop.domain.User;
import com.melly.bloomingshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserManageService {
    private final UserRepository userRepository;

    // 모든 회원 정보 및 페이징 처리
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
