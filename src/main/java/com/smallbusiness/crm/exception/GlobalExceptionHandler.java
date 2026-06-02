package com.smallbusiness.crm.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        e.printStackTrace();
        model.addAttribute("errorMessage", "Произошла непредвиденная ошибка: " + e.getMessage());
        return "error";
    }
}
