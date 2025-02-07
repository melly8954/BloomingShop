package com.melly.bloomingshop.controller;

import com.melly.bloomingshop.domain.User;
import com.melly.bloomingshop.security.auth.PrincipalDetails;
import com.melly.bloomingshop.security.config.SecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class IndexController {
    @GetMapping("/")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof PrincipalDetails principalDetails) {
                User loginUser = principalDetails.getUser();
                model.addAttribute(SecurityConfig.LOGINUSER, loginUser);
                model.addAttribute("isAuthenticated", true);

                if ("ADMIN".equals(loginUser.getRoleId().getRoleName())) {
                    model.addAttribute("isAdmin", true);  // Mustache에서 사용할 값 추가
                } else {
                    model.addAttribute("isAdmin", false);
                }

            } else if (principal instanceof DefaultOAuth2User oauthUser) {
                String name = oauthUser.getAttribute("name");
                model.addAttribute(SecurityConfig.LOGINUSER, name);
                model.addAttribute("isAuthenticated", true);
                model.addAttribute("isAdmin", false);
            } else {
                model.addAttribute("isAuthenticated", false);
                model.addAttribute("isAdmin", false);
            }
        } else {
            model.addAttribute("isAuthenticated", false);
            model.addAttribute("isAdmin", false);
        }
        return "index";
    }

}
