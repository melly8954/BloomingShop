package com.melly.bloomingshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/home")
    public String adminPage() {
        return "admin/admin_home";
    }

    @GetMapping("/product/list")
    public String productPage() {
        return "admin/product/product_list_admin";
    }

    @GetMapping("/product/add")
    public String productAddPage() {
        return "admin/product/product_add_admin";
    }


}
