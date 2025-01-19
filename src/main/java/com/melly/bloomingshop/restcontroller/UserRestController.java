package com.melly.bloomingshop.restcontroller;

import com.melly.bloomingshop.common.ResponseController;
import com.melly.bloomingshop.common.ResponseDto;
import com.melly.bloomingshop.domain.User;
import com.melly.bloomingshop.dto.*;
import com.melly.bloomingshop.security.auth.PrincipalDetails;
import com.melly.bloomingshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserRestController implements ResponseController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@Validated @RequestBody RegisterRequest registerRequest, BindingResult bindingResult ) {
        //  유효성 검사는 데이터가 비즈니스 로직에 들어가기 전에 검증하는 것
        if(bindingResult.hasErrors()){
            // 유효성 검사를 실패(@Size , @NotBlank 조건 실패 시) 하면 BindingResult 객체에 오류가 담긴다.
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append(" / ");
            });
            log.error("유효성 검사 실패" + errorMessages);
            return makeResponseEntity(HttpStatus.BAD_REQUEST,errorMessages.toString(), null);
        }
        try { // 비즈니스 로직 시작
            User user = this.userService.registerUserWithAddress(registerRequest);
            return makeResponseEntity(HttpStatus.OK,"회원 가입 성공", user);
        }catch (IllegalArgumentException e) {
                // 닉네임 중복과 같은 비즈니스 로직 오류 처리
                log.error(e.getMessage());
                return makeResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage(), null); // 400 Bad Request
        }catch (Exception ex) {
            log.error(ex.getMessage());
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(),null);
        }
    }


    // 로그인아이디 중복 체크 API
    @PostMapping("/check-loginId")
    public ResponseEntity<Boolean> checkLoginId(@RequestBody LoginIdCheckDto loginIdCheckDto) {
        boolean isLoginIdExist = userService.isLoginIdExist(loginIdCheckDto.getLoginId());
        return ResponseEntity.ok(isLoginIdExist);
    }

    // 이메일 중복 체크 API
    @PostMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestBody MailRequest emailRequest) {
        boolean isEmailExist = userService.isEmailExist(emailRequest.getMail());
        return ResponseEntity.ok(isEmailExist);
    }

    // 휴대전화 중복 체크 API
    @PostMapping("/check-phoneNumber")
    public ResponseEntity<Boolean> checkPhoneNumber(@RequestBody PhoneNumberCheckDto phoneNumberCheckDto) {
        boolean isPhoneNumberExist = userService.isPhoneNumberExist(phoneNumberCheckDto.getPhoneNumber());
        return ResponseEntity.ok(isPhoneNumberExist);
    }


    // 이메일로 로그인 아이디 찾기
    @GetMapping("/login-id/{email}")
    public ResponseEntity<ResponseDto> findLoginIdByEmail(@PathVariable("email") String email) {
        // 이메일이 null 이거나 비어 있는 경우 처리
        if(email == null || email.isEmpty()){
            log.error("email -> null 이거나 비어있음");
            return makeResponseEntity(HttpStatus.BAD_REQUEST,"email 은 필수 입력 항목입니다.",null);
        }
        // 이메일 형식의 정규표현식을 검사해주는 부분
        if(!email.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")){
            log.error("email 형식 오류");
            return makeResponseEntity(HttpStatus.BAD_REQUEST,"email 형식을 맞추셔야 합니다.",null);
        }
        try{
            String loginIdByEmail = this.userService.getLoginIdByEmail(email);
            return makeResponseEntity(HttpStatus.OK,"아이디 찾기 성공",loginIdByEmail);
        }catch (Exception ex){
            log.error(ex.getMessage());
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(),null);
        }
    }

    // 로그인 상태에서 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity<ResponseDto> changePassword(@Validated @RequestBody ChangePasswordRequestDto changePasswordRequestDto, BindingResult bindingResult) {
        try {
            // 유효성 검사 실패 시
            if (bindingResult.hasErrors()) {
                StringBuilder errorMessages = new StringBuilder();
                bindingResult.getAllErrors().forEach(error -> {
                    errorMessages.append(error.getDefaultMessage()).append(" / ");
                });
                log.error("유효성 검사 실패" + errorMessages);
                return makeResponseEntity(HttpStatus.BAD_REQUEST,errorMessages.toString(), null);
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                log.error("사용자 인증이 처리되지 않음 -> 다시 로그인 필요");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 인증되지 않은 경우
            }
            // 로그인한 사용자의 이메일 가져오기
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            String email = principalDetails.getEmail(); // 이메일 가져오기

            // 현재 비밀번호와 신규 비밀번호 처리
            String currentPassword = changePasswordRequestDto.getCurrentPassword();
            String newPassword = changePasswordRequestDto.getNewPassword();

            // 비밀번호 검증 (예: 현재 비밀번호 확인 및 새 비밀번호 저장)
            boolean passwordValid = userService.checkCurrentPassword(email, currentPassword);
            if (!passwordValid) {
                log.error("현재 비밀번호가 일치하지 않습니다.");
                return makeResponseEntity(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다.", null);
            }

            // 새 비밀번호로 업데이트
            userService.updatePassword(email, newPassword);

            return makeResponseEntity(HttpStatus.OK,"비밀번호 변경 완료",true);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류: " + ex.getMessage(), null);
        }
    }
}
