package com.melly.bloomingshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RegisterRequest {
    @NotBlank(message = "로그인 ID 항목은 필수 입력 항목입니다.")
    @Size(min = 5, max = 20, message = "로그인 ID는 5~20자 사이 입니다.")
    private String loginId;

    @NotBlank(message = "비밀번호 항목은 필수 입력 항목입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이 입니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인 항목은 필수 입력 항목입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이 입니다.")
    private String confirmPassword;

    @NotBlank(message = "이름 항목은 필수 입력 항목입니다.")
    @Size(min = 2, max = 100, message = "이름은 2~100자 사이 입니다.")
    private String name;

    private String gender;

    private LocalDate birthdate;

    @NotBlank(message = "email 항목은 필수 입력 항목입니다.")
    @Size(min = 10, max = 255, message = "email은 10~255자 사이 입니다.")
    private String email;

    private String phoneNumber;

    // 주소 정보
    @NotBlank(message = "우편번호는 필수 입력 값입니다.")
    private String postcode;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;

    private String detailAddress;
}
