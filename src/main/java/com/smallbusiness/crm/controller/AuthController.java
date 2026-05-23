package com.smallbusiness.crm.controller;

import com.smallbusiness.crm.dto.UserRegistrationDto;
import com.smallbusiness.crm.entity.Role;
import com.smallbusiness.crm.entity.User;
import com.smallbusiness.crm.repository.RoleRepository;
import com.smallbusiness.crm.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDto") UserRegistrationDto userDto,
                               BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        if (userRepository.existsByUsername(userDto.getUsername())) {
            model.addAttribute("error", "Пользователь с таким логином уже существует");
            return "auth/register";
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            model.addAttribute("error", "Пользователь с таким email уже существует");
            return "auth/register";
        }

        // Создаем сущность пользователя
        User user = new User();
        user.setUsername(userDto.getUsername());
        // Хэшируем пароль перед сохранением в БД
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        // По умолчанию даем роль MANAGER (она создается скриптом миграции)
        Role defaultRole = roleRepository.findByName("MANAGER")
                .orElseThrow(() -> new RuntimeException("Роль MANAGER не найдена в базе данных"));
        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);

        return "redirect:/auth/login?success";
    }
}