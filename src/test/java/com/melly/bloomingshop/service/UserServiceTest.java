package com.melly.bloomingshop.service;

import com.melly.bloomingshop.domain.Address;
import com.melly.bloomingshop.domain.Role;
import com.melly.bloomingshop.domain.StatusType;
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

import static org.assertj.core.api.Assertions.assertThat;
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
        RegisterRequest request = new RegisterRequest(
                "loginId", "password", "password", "name", "male",
                LocalDate.of(1990, 1, 1), "email@example.com", "010-1234-1234",
                "12345", "address", "detailAddress"
        );

        // 기본 'USER' 역할 설정
        Role userRole = Role.builder()
                .role("USER")
                .build();

        // Mocking: 중복 검사 결과 false
        when(userRepository.existsByLoginId(request.getLoginId())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);

        // Mocking: 비밀번호 암호화
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // Mocking: 기본 역할 'USER' 설정
        when(roleRepository.findByRole("USER")).thenReturn(java.util.Optional.of(userRole));

        // Mocking: 저장된 User 객체 설정
        User mockedUser = User.builder()
                .loginId(request.getLoginId())
                .password("encodedPassword")
                .name(request.getName())
                .gender(request.getGender())
                .birthdate(request.getBirthdate())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .roleId(userRole)
                .status(StatusType.ACTIVE)
                .build();
        when(userRepository.save(any(User.class))).thenReturn(mockedUser);

        // Mocking: 저장된 Address 객체 설정
        Address mockedAddress = new Address();
        mockedAddress.setPostcode(request.getPostcode());
        mockedAddress.setAddress(request.getAddress());
        mockedAddress.setDetailAddress(request.getDetailAddress());
        mockedAddress.setUser(mockedUser);
        when(addressRepository.save(any(Address.class))).thenReturn(mockedAddress);

        // When
        User savedUser = userService.registerUserWithAddress(request);

        // Then
        // 저장된 User의 필드 값 검증
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getLoginId()).isEqualTo(request.getLoginId());
        assertThat(savedUser.getEmail()).isEqualTo(request.getEmail());
        assertThat(savedUser.getName()).isEqualTo(request.getName());
        assertThat(savedUser.getGender()).isEqualTo(request.getGender());
        assertThat(savedUser.getBirthdate()).isEqualTo(request.getBirthdate());
        assertThat(savedUser.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword"); // 암호화된 비밀번호 확인
        assertThat(savedUser.getRoleId().getRole()).isEqualTo("USER"); // 역할 확인
        assertThat(savedUser.getStatus()).isEqualTo(StatusType.ACTIVE); // 상태 확인

        // addressRepository에 저장된 주소 데이터 검증
        verify(addressRepository).save(argThat(address ->
                address.getPostcode().equals(request.getPostcode()) &&
                address.getAddress().equals(request.getAddress()) &&
                address.getDetailAddress().equals(request.getDetailAddress()) &&
                address.getUser().getLoginId().equals(request.getLoginId()) // 연결된 User 확인
        ));

        // userRepository.save와 addressRepository.save가 정확히 한 번씩 호출되었는지 확인
        verify(userRepository, times(1)).save(any(User.class));
        verify(addressRepository, times(1)).save(any(Address.class));
    }


    @Test
    @DisplayName("로그인 아이디 중복 여부 확인 - 존재하는 경우")
    void isLoginIdExist_shouldReturnTrueWhenLoginIdExists() {
        // Given
        String loginId = "existingLoginId";

        // Mocking
        when(userRepository.existsByLoginId(loginId)).thenReturn(true);

        // When
        boolean result = userService.isLoginIdExist(loginId);

        // Then
        assertThat(result).isTrue(); // 로그인 ID가 존재한다고 확인
        verify(userRepository, times(1)).existsByLoginId(loginId);
    }

    @Test
    @DisplayName("로그인 아이디 중복 여부 확인 - 존재하지 않는 경우")
    void isLoginIdExist_shouldReturnFalseWhenLoginIdDoesNotExist() {
        // Given
        String loginId = "nonExistingLoginId";

        // Mocking
        when(userRepository.existsByLoginId(loginId)).thenReturn(false);

        // When
        boolean result = userService.isLoginIdExist(loginId);

        // Then
        assertThat(result).isFalse(); // 로그인 ID가 존재하지 않는다고 확인
        verify(userRepository, times(1)).existsByLoginId(loginId);
    }

    @Test
    @DisplayName("이메일 중복 여부 확인 - 존재하는 경우")
    void isEmailExist_shouldReturnTrueWhenEmailExists() {
        // Given
        String email = "existingEmail@example.com";

        // Mocking
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // When
        boolean result = userService.isEmailExist(email);

        // Then
        assertThat(result).isTrue(); // 이메일이 존재한다고 확인
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    @DisplayName("이메일 중복 여부 확인 - 존재하지 않는 경우")
    void isEmailExist_shouldReturnFalseWhenEmailDoesNotExist() {
        // Given
        String email = "nonExistingEmail@example.com";

        // Mocking
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // When
        boolean result = userService.isEmailExist(email);

        // Then
        assertThat(result).isFalse(); // 이메일이 존재하지 않는다고 확인
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    @DisplayName("휴대전화번호 중복 여부 확인 - 존재하는 경우")
    void isPhoneNumberExist_shouldReturnTrueWhenPhoneNumberExists() {
        // Given
        String phoneNumber = "01012345678";

        // Mocking
        when(userRepository.existsByPhoneNumber(phoneNumber)).thenReturn(true);

        // When
        boolean result = userService.isPhoneNumberExist(phoneNumber);

        // Then
        assertThat(result).isTrue(); // 전화번호가 존재한다고 확인
        verify(userRepository, times(1)).existsByPhoneNumber(phoneNumber);
    }

    @Test
    @DisplayName("휴대전화번호 중복 여부 확인 - 존재하지 않는 경우")
    void isPhoneNumberExist_shouldReturnFalseWhenPhoneNumberDoesNotExist() {
        // Given
        String phoneNumber = "01087654321";

        // Mocking
        when(userRepository.existsByPhoneNumber(phoneNumber)).thenReturn(false);

        // When
        boolean result = userService.isPhoneNumberExist(phoneNumber);

        // Then
        assertThat(result).isFalse(); // 전화번호가 존재하지 않는다고 확인
        verify(userRepository, times(1)).existsByPhoneNumber(phoneNumber);
    }
}