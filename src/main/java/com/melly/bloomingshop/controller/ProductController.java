package com.melly.bloomingshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {
    @GetMapping("/product")
    public String product() {
        return "product/product_list";
    }
}
