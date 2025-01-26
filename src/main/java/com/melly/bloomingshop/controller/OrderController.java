package com.melly.bloomingshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
    @GetMapping("/order/list")
    public String orderPage() {
        return "order/order_list";
    }
}
