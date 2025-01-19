package com.melly.bloomingshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {
    @GetMapping("/register")
    public String registerPage() {
        return "user/register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    // OAuth2 로그인 실패 시 redirect 되는 view , CustomAuthenticationFailureHandler 가 관리
    @GetMapping("/login-fail")
    public String loginFail(@RequestParam("error") String errorMessage, Model model) {
        model.addAttribute("error", errorMessage);
        return "user/login_fail";  // Mustache template file
    }

    @GetMapping("/find-id")
    public String findIdPage(){
        return "user/find_id";
    }

    @GetMapping("/find-password")
    public String findPasswordPage(){
        return "user/find_password";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(){
        return "user/change_password";
    }
}
