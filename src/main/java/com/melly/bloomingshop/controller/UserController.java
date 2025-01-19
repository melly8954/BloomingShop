package com.melly.bloomingshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @GetMapping("/user/register")
    public String registerPage() {
        return "user/register";
    }

    @GetMapping("/user/login")
    public String loginPage() {
        return "user/login";
    }

    // OAuth2 로그인 실패 시 redirect 되는 view , CustomAuthenticationFailureHandler 가 관리
    @GetMapping("/user/login-fail")
    public String loginFail(@RequestParam("error") String errorMessage, Model model) {
        model.addAttribute("error", errorMessage);
        return "user/login_fail";  // Mustache template file
    }

}
