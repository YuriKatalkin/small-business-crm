package com.smallbusiness.crm.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        // Логируем ошибку, чтобы ты видел причину в консоли IDEA
        e.printStackTrace();

        // Передаем сообщение об ошибке на страницу
        model.addAttribute("errorMessage", "Произошла непредвиденная ошибка: " + e.getMessage());
        return "error";
    }
}