package com.smallbusiness.crm.controller;

import com.smallbusiness.crm.entity.User;
import com.smallbusiness.crm.entity.Role;
import com.smallbusiness.crm.service.UserService;
import com.smallbusiness.crm.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        try {
            userService.registerUser(user, "USER");
            return "redirect:/auth/login?success";
        } catch (RuntimeException e) {
            return "redirect:/auth/register?error=" + e.getMessage();
        }
    }
}
