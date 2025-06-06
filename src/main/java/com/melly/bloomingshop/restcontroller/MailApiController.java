package com.melly.bloomingshop.restcontroller;

import com.melly.bloomingshop.dto.PasswordVerificationRequest;
import com.melly.bloomingshop.service.MailService;
import com.melly.bloomingshop.service.UserService;
import com.melly.bloomingshop.dto.MailRequest;
import com.melly.bloomingshop.dto.MailVerificationRequest;
import com.melly.bloomingshop.service.MailService;
import com.melly.bloomingshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@EnableAsync
public class MailApiController {
    private final MailService mailService;
    private final UserService userService;

    /**
     * 인증번호 발송 메소드
     */
    @PostMapping("/api/mail")
    public CompletableFuture<String> mailSend(@RequestBody MailRequest mailRequest) {
        return mailService.sendMail(mailRequest.getMail())
                .thenApply(number -> String.valueOf(number));
    }

    /**
     * 인증번호 검증 메소드
     */
    @PostMapping("/api/mail/verify-code")
    public String verifyCode(@RequestBody MailVerificationRequest verificationRequest) {
        boolean isVerified = mailService.verifyCode(verificationRequest.getMail(), verificationRequest.getCode());
        return isVerified ? "Verified" : "Verification failed";
    }

    /**
     * 임시 비밀번호 재발급 발송 메서드
     */
    @PostMapping("/api/mail/temp-password")
    public ResponseEntity<String> tempPassword(@RequestBody MailRequest mailRequest) {
        String email = mailRequest.getMail();

        if (userService.isEmailExist(mailRequest.getMail())) {
            String tempPassword = mailService.createTemporaryPassword(email);
            mailService.sendTemporaryPasswordMail(email, tempPassword);
            return ResponseEntity.ok("임시 비밀번호가 이메일로 발송되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 이메일로 가입된 사용자가 없습니다.");
        }
    }

    /**
     * 임시 비밀번호 검증 메소드
     */
    @PostMapping("/api/mail/verify-temporary-password")
    public ResponseEntity<String> verifyTemporaryPassword(@RequestBody PasswordVerificationRequest request) {
        boolean isVerified = mailService.verifyTemporaryPassword(request.getMail(), request.getTempPassword());
        return isVerified ? ResponseEntity.ok("Verified") : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification failed");
    }
}

