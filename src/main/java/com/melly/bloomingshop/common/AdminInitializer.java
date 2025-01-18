package com.melly.bloomingshop.common;

import com.melly.bloomingshop.domain.Role;
import com.melly.bloomingshop.domain.StatusType;
import com.melly.bloomingshop.domain.User;
import com.melly.bloomingshop.repository.RoleRepository;
import com.melly.bloomingshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // db에 "admin" 역할 존재 확인 , 없으면 추가
        Optional<Role> adminRoleOptional = roleRepository.findByRole("ADMIN");
        Role adminRole;
        adminRole = adminRoleOptional.orElseGet(
                () -> roleRepository.save(Role.builder().role("ADMIN").build()) );

        // "admin" 사용자가 이미 존재하는지 확인
        if( !userRepository.existsByLoginId("testidsuper")){
            User admin = User.builder()
                    .loginId("testidsuper")
                    .password(passwordEncoder.encode("1q2w3e4r!"))
                    .name("administrator")
                    .gender("남성")
                    .birthdate(LocalDate.of(1998, 10, 8))  // 관리자 생일 설정
                    .email("admin@example.com")          // 관리자 이메일 설정
                    .phoneNumber("010-1234-5678")        // 관리자 전화번호 설정
                    .roleId(adminRole) // 역할 ID 설정
                    .status(StatusType.ACTIVE)                   // 상태 설정
                    .createdDate(LocalDateTime.now())        // 생성일 설정
                    .lastLogin(null)                     // 마지막 로그인 초기값
                    .disabledDate(null)                  // 비활성화 날짜 초기값
                    .deletedDate(null)                   // 삭제 날짜 초기값
                    .build();
            userRepository.save(admin); // 관리자 계정 저장
        }
    }
}
