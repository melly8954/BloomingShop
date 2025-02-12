package com.melly.bloomingshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {
    @GetMapping("/support/list")
    public String support() {
        return "board/support_list";
    }

    @GetMapping("/support/register")
    public String register() {
        return "board/support_register";
    }

    @GetMapping("/support/view/{boardId}")
    public String register(@PathVariable Long boardId) {
        return "board/support_view";
    }
}
