package com.melly.bloomingshop.service;

import com.melly.bloomingshop.domain.Role;
import com.melly.bloomingshop.domain.StatusType;
import com.melly.bloomingshop.domain.User;
import com.melly.bloomingshop.dto.RegisterDto;
import com.melly.bloomingshop.repository.RoleRepository;
import com.melly.bloomingshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegisterDto registerDto) {
        // 해당 필드 중복체크
        if(userRepository.existsByLoginId(registerDto.getLoginId())){
            // 서비스에서 예외를 던지는 방식은 비즈니스 로직에 문제가 생겼을 때 이를 명확하게 클라이언트에 전달하는 좋은 방법
            throw new IllegalArgumentException("이미 사용 중인 로그인 ID 입니다.");
        }
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new IllegalArgumentException("이미 사용 중인 email 입니다.");
        }
        if(userRepository.existsByPhoneNumber(registerDto.getPhoneNumber())){
            throw new IllegalArgumentException("이미 사용 중인 전화번호 입니다.");
        }

        // 이메일 형식의 정규표현식을 검사해주는 부분
        if(!registerDto.getEmail().matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")){
            throw new IllegalArgumentException("email 형식을 맞추셔야 합니다.");
        }

        // 비밀번호와 비밀번호 확인 일치 여부 확인
        if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())){
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

        // 기본 'USER' 역할 할당 (관리자 등 역할은 추후 확장 가능)
        Role userRole = roleRepository.findByRole("USER")
                .orElseThrow(() -> new IllegalArgumentException("기본 역할이 존재하지 않습니다."));

        User user = User.builder()
                .loginId(registerDto.getLoginId())
                .password(encodedPassword)
                .name(registerDto.getName())
                .gender(registerDto.getGender())
                .birthdate(registerDto.getBirthdate())
                .email(registerDto.getEmail())
                .phoneNumber(registerDto.getPhoneNumber())
                .address(registerDto.getAddress())
                .roleId(userRole)
                .status(StatusType.ACTIVE)
                .build();
        User save = this.userRepository.save(user);
        return save;
    }

    public boolean isLoginIdExist(RegisterDto registerDto){
        return this.userRepository.existsByLoginId(registerDto.getLoginId());
    }
}
