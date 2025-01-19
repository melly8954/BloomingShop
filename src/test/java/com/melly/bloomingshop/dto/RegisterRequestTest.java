package com.melly.bloomingshop.dto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("유효한 RegisterRequest 객체는 유효성 검사를 통과한다")
    void testValidRegisterRequest() {
        // Given: 유효한 DTO 생성
        RegisterRequest request = RegisterRequest.builder()
                .loginId("validLoginId")
                .password("validPass123")
                .confirmPassword("validPass123")
                .name("홍길동")
                .gender("male")
                .birthdate(LocalDate.of(1990, 1, 1))
                .email("test@example.com")
                .phoneNumber("01012345678")
                .postcode("12345")
                .address("서울시 강남구")
                .detailAddress("101호")
                .build();

        // When: 유효성 검사
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then: 유효성 검사 통과
        assertTrue(violations.isEmpty(), "유효한 RegisterRequest 객체는 유효성 검사를 통과해야 합니다.");
    }

    @Test
    @DisplayName("유효하지 않은 RegisterRequest 객체는 유효성 검사에서 실패한다")
    void testInvalidRegisterRequest() {
        // Given: 유효하지 않은 DTO 생성
        RegisterRequest request = RegisterRequest.builder()
                .loginId("") // 비어있는 loginId
                .password("short") // 짧은 비밀번호
                .confirmPassword("") // 비어있는 비밀번호 확인
                .name("") // 비어있는 이름
                .email("") // 잘못된 이메일 형식
                .postcode("") // 비어있는 우편번호
                .address("") // 비어있는 주소
                .build();

        // When: 유효성 검사
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        // Then: 유효성 검사 실패
        assertFalse(violations.isEmpty(), "유효하지 않은 RegisterRequest 객체는 유효성 검사에 실패해야 합니다.");

        // 개별 필드의 에러 메시지 확인
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("로그인 ID 항목은 필수 입력 항목입니다.")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("비밀번호는 8~20자 사이 입니다.")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("이름 항목은 필수 입력 항목입니다.")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("email 항목은 필수 입력 항목입니다.")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("우편번호는 필수 입력 값입니다.")));
    }

    @Test
    @DisplayName("RegisterRequest 필드 값이 올바르게 매핑된다")
    void testFieldMapping() {
        // Given
        String loginId = "testLogin";
        String password = "password123";
        String confirmPassword = "password123";
        String name = "테스트";
        String email = "test@example.com";
        String postcode = "12345";
        String address = "서울시 강남구";
        String detailAddress = "101호";

        // When
        RegisterRequest request = RegisterRequest.builder()
                .loginId(loginId)
                .password(password)
                .confirmPassword(confirmPassword)
                .name(name)
                .email(email)
                .postcode(postcode)
                .address(address)
                .detailAddress(detailAddress)
                .build();

        // Then
        assertEquals(loginId, request.getLoginId(), "loginId 필드 값이 올바르게 매핑되어야 합니다.");
        assertEquals(password, request.getPassword(), "password 필드 값이 올바르게 매핑되어야 합니다.");
        assertEquals(confirmPassword, request.getConfirmPassword(), "confirmPassword 필드 값이 올바르게 매핑되어야 합니다.");
        assertEquals(name, request.getName(), "name 필드 값이 올바르게 매핑되어야 합니다.");
        assertEquals(email, request.getEmail(), "email 필드 값이 올바르게 매핑되어야 합니다.");
        assertEquals(postcode, request.getPostcode(), "postcode 필드 값이 올바르게 매핑되어야 합니다.");
        assertEquals(address, request.getAddress(), "address 필드 값이 올바르게 매핑되어야 합니다.");
        assertEquals(detailAddress, request.getDetailAddress(), "detailAddress 필드 값이 올바르게 매핑되어야 합니다.");
    }
}