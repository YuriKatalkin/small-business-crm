package com.smallbusiness.crm.controller;

import com.smallbusiness.crm.entity.User;
import com.smallbusiness.crm.repository.UserRepository;
import com.smallbusiness.crm.service.ContactService;
import com.smallbusiness.crm.service.DealService;
import com.smallbusiness.crm.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserRepository userRepository;
    private final DealService dealService;
    private final ContactService contactService;
    private final TaskService taskService;

    @GetMapping("/dashboard")
    public String showDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // Получаем текущего пользователя
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Добавляем данные в модель
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("contactCount", contactService.getAllContacts(currentUser).size());
        model.addAttribute("wonAmount", dealService.getTotalWonAmount(currentUser));
        model.addAttribute("pipelineAmount", dealService.getPipelineAmount(currentUser));
        model.addAttribute("completedTasks", taskService.getCompletedTasksCount(currentUser));

        return "dashboard";
    }
}