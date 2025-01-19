package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 로그인 ID로 계정 중복 검사
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    // 로그인 아이디로 사용자 찾기
    User findByLoginId(String loginId);

    // 이메일로 사용자 찾기
    User findByEmail(String email);
}
