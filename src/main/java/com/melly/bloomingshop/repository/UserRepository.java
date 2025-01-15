package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 로그인 ID로 계정 중복 검사
    boolean existsByLoginId(String loginId);
}
