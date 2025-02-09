package com.melly.bloomingshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SupportBoardController {
    @GetMapping("/support/list")
    public String support() {
        return "/support/support_list";
    }

    @GetMapping("/support/register")
    public String register() {
        return "/support/support_register";
    }

    @GetMapping("/support/view/{boardId}")
    public String register(@PathVariable Long boardId) {
        return "/support/support_view";
    }
}
