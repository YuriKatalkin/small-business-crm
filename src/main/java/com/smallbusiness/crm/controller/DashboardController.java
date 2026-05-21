package com.smallbusiness.crm.controller;

import com.smallbusiness.crm.service.ContactService;
import com.smallbusiness.crm.service.DealService;
import com.smallbusiness.crm.service.TaskService;
import com.smallbusiness.crm.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final ContactService contactService;
    private final DealService dealService;
    private final TaskService taskService;
    private final CompanyService companyService;

    @GetMapping
    public String dashboard(Model model, Authentication authentication) {
        // TODO: get current user
        model.addAttribute("totalContacts", 0);
        model.addAttribute("totalCompanies", 0);
        model.addAttribute("totalDeals", 0);
        model.addAttribute("pipelineAmount", dealService.getPipelineAmount(null));
        model.addAttribute("wonAmount", dealService.getTotalWonAmount(null));
        model.addAttribute("recentContacts", contactService.getAllContacts(null));
        model.addAttribute("pendingTasks", taskService.getTasksByStatus(null, null));
        return "dashboard";
    }
}
