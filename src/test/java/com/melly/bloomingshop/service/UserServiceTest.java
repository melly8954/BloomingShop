package com.melly.bloomingshop.service;

import com.melly.bloomingshop.domain.Address;
import com.melly.bloomingshop.domain.Role;
import com.melly.bloomingshop.domain.User;
import com.melly.bloomingshop.dto.RegisterRequest;
import com.melly.bloomingshop.repository.AddressRepository;
import com.melly.bloomingshop.repository.RoleRepository;
import com.melly.bloomingshop.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자가 정상적으로 등록되는지 확인하는 테스트")
    void registerUserWithAddress_shouldRegisterUserSuccessfully() {
        // Given
        RegisterRequest request = new RegisterRequest("loginId", "password", "password", "name", "male", LocalDate.of(1990, 1, 1), "email@example.com", "1234567890", "12345", "address", "detailAddress");

        // 기본 'USER' 역할 설정
        Role userRole = Role.builder()
                .role("USER")
                .build();

        // Mocking: userRepository.existsByLoginId, existsByEmail, existsByPhoneNumber가 호출되면 모두 false를 반환하도록 설정
        when(userRepository.existsByLoginId(request.getLoginId())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);

        // 비밀번호 암호화
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // Mocking: roleRepository.findByRole가 호출되면 'USER' 역할을 반환하도록 설정
        when(roleRepository.findByRole("USER")).thenReturn(java.util.Optional.of(userRole));

        // Mocking: userRepository.save가 호출되면 새로운 User 객체를 반환하도록 설정
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Mocking: addressRepository.save가 호출되면 아무것도 반환하지 않도록 설정
        when(addressRepository.save(any(Address.class))).thenReturn(new Address());

        // When
        User savedUser = userService.registerUserWithAddress(request);

        // Then
        // 유저가 정상적으로 저장되었는지 확인
        assertNotNull(savedUser);

        // userRepository.save와 addressRepository.save가 정확히 한번씩 호출되었는지 확인
        verify(userRepository, times(1)).save(any(User.class));
        verify(addressRepository, times(1)).save(any());
    }

    @Test
    void isLoginIdExist() {
    }

    @Test
    void isEmailExist() {
    }

    @Test
    void isPhoneNumberExist() {
    }
}