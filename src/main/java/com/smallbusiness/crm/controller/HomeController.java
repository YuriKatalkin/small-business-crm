package com.smallbusiness.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        // Перенаправляем пользователя на страницу сделок при входе
        return "redirect:/deals";
    }
}