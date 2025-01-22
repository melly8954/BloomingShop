package com.melly.bloomingshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/product/modify/{productId}")
    public String productModifyPage(@PathVariable Long productId, Model model) {
        // 필요하면 productId를 기반으로 데이터 로드 후 Model에 추가
        model.addAttribute("productId", productId);
        return "admin/product/product_modify_admin";
    }
}
