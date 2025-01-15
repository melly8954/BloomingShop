package com.melly.bloomingshop.restcontroller;

import com.melly.bloomingshop.common.ResponseController;
import com.melly.bloomingshop.common.ResponseDto;
import com.melly.bloomingshop.domain.User;
import com.melly.bloomingshop.dto.RegisterDto;
import com.melly.bloomingshop.repository.UserRepository;
import com.melly.bloomingshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserRestController implements ResponseController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@Validated @RequestBody RegisterDto registerDto, BindingResult bindingResult ) {
        //  유효성 검사는 데이터가 비즈니스 로직에 들어가기 전에 검증하는 것
        if(bindingResult.hasErrors()){
            // 유효성 검사를 실패(@Size , @NotBlank 조건 실패 시) 하면 BindingResult 객체에 오류가 담긴다.
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append(" / ");
            });
            return makeResponseEntity(HttpStatus.BAD_REQUEST,errorMessages.toString(), null);
        }
        try { // 비즈니스 로직 시작
            User user = this.userService.registerUser(registerDto);
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
}
