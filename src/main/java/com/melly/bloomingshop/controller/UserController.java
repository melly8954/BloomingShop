package com.melly.bloomingshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
