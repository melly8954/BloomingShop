package com.melly.bloomingshop.service;

import com.melly.bloomingshop.domain.Address;
import com.melly.bloomingshop.domain.Role;
import com.melly.bloomingshop.domain.StatusType;
import com.melly.bloomingshop.domain.User;
import com.melly.bloomingshop.dto.RegisterRequest;
import com.melly.bloomingshop.dto.UserAddress;
import com.melly.bloomingshop.repository.AddressRepository;
import com.melly.bloomingshop.repository.RoleRepository;
import com.melly.bloomingshop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addresRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public User registerUserWithAddress(RegisterRequest registerRequest) {
        // 해당 필드 중복체크
        if(userRepository.existsByLoginId(registerRequest.getLoginId())){
            // 서비스에서 예외를 던지는 방식은 비즈니스 로직에 문제가 생겼을 때 이를 명확하게 클라이언트에 전달하는 좋은 방법
            throw new IllegalArgumentException("이미 사용 중인 로그인 ID 입니다.");
        }
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new IllegalArgumentException("이미 사용 중인 email 입니다.");
        }
        if(userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())){
            throw new IllegalArgumentException("이미 사용 중인 전화번호 입니다.");
        }

        // 이메일 형식의 정규표현식을 검사해주는 부분
        if(!registerRequest.getEmail().matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")){
            throw new IllegalArgumentException("email 형식을 맞추셔야 합니다.");
        }

        // 비밀번호와 비밀번호 확인 일치 여부 확인
        if(!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())){
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        // 기본 'USER' 역할 할당 (관리자 등 역할은 추후 확장 가능)
        Role userRole = roleRepository.findByRole("USER")
                .orElseThrow(() -> new IllegalArgumentException("기본 역할이 존재하지 않습니다."));
        
        // 유저 생성
        User user = User.builder()
                .loginId(registerRequest.getLoginId())
                .password(encodedPassword)
                .name(registerRequest.getName())
                .gender(registerRequest.getGender())
                .birthdate(registerRequest.getBirthdate())
                .email(registerRequest.getEmail())
                .phoneNumber(registerRequest.getPhoneNumber())
                .roleId(userRole)
                .status(StatusType.ACTIVE)
                .build();
        User save = this.userRepository.save(user);
        
        // 해당 유저의 주소 저장
        Address address = new Address();
        address.setUser(user); // `user_id` 참조
        address.setPostcode(registerRequest.getPostcode());
        address.setAddress(registerRequest.getAddress());
        address.setDetailAddress(registerRequest.getDetailAddress());
        addressRepository.save(address);

        return save;
    }

    // 로그인 아이디 중복 여부 확인
    public boolean isLoginIdExist(String loginId){
        return this.userRepository.existsByLoginId(loginId);
    }
    // 이메일 중복 여부 확인
    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    // 휴대전환 중복 여부 확인
    public boolean isPhoneNumberExist(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }


    // 이메일로 로그인 아이디 찾기
    public String getLoginIdByEmail(String email) {
        User user = this.userRepository.findByEmail(email);
        return user.getLoginId();
    }


    // 현재 비밀번호가 맞는지 검증
    public boolean checkCurrentPassword(String email, String currentPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false; // 이메일에 해당하는 사용자가 없으면 false 반환
        }
        // BCryptPasswordEncoder로 암호화된 비밀번호 비교
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    // 새 비밀번호로 업데이트
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            // 새 비밀번호 암호화
            user.changePassword(passwordEncoder.encode(newPassword));
            userRepository.save(user); // 비밀번호 업데이트
        }
    }

    // 유저의 주소를 찾는 비즈니스 로직
    @Transactional
    public UserAddress getUserAddress(String loginId) {
        User user = userRepository.findByLoginId(loginId); // 유저 정보 조회
        Address address = addressRepository.findByUserId(user.getId()); // 유저의 주소 조회

        return new UserAddress(address); // 주소 정보를 DTO로 반환
    }

    // 사용자 or 관리자가 자신의 계정을 탈퇴 (삭제 처리)
    @Transactional
    public void softDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        // 사용자가 자신을 삭제할 때, 계정을 삭제된 상태로 변경
        user.changeStatus(StatusType.DELETED); // 상태를 DELETED로 변경
        user.changeDeletedDate(LocalDateTime.now()); // 삭제 날짜를 현재 시간으로 설정
        userRepository.save(user); // 변경 사항 저장
    }
}
