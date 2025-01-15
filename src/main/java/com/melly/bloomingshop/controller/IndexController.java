package com.melly.bloomingshop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Configuration
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
