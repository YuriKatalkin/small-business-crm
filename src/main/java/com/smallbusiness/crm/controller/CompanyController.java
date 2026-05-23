package com.smallbusiness.crm.controller;

import com.smallbusiness.crm.entity.Company;
import com.smallbusiness.crm.entity.User;
import com.smallbusiness.crm.security.CustomUserDetails;
import com.smallbusiness.crm.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    private User extractUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

    @GetMapping
    public String listCompanies(Model model, @RequestParam(required = false) String search, Authentication authentication) {
        User currentOwner = extractUser(authentication);
        // Исправлено: аргументы приведены к порядку (searchTerm, owner) как в сервисе
        model.addAttribute("companies", companyService.searchCompanies(search, currentOwner));
        model.addAttribute("search", search);
        return "companies/list";
    }

    @GetMapping("/new")
    public String createCompanyForm(Model model) {
        model.addAttribute("company", new Company());
        return "companies/form";
    }

    @PostMapping
    public String saveCompany(@ModelAttribute Company company, Authentication authentication) {
        company.setOwner(extractUser(authentication));
        companyService.createCompany(company);
        return "redirect:/companies";
    }

    @GetMapping("/{id}/edit")
    public String editCompanyForm(@PathVariable Long id, Model model, Authentication authentication) {
        User currentOwner = extractUser(authentication);
        // Исправлено: имя метода изменено на getCompanyById в соответствии с CompanyService
        Company company = companyService.getCompanyById(id, currentOwner)
                .orElseThrow(() -> new IllegalArgumentException("Компания не найдена или доступ запрещен"));
        model.addAttribute("company", company);
        return "companies/form";
    }

    @PostMapping("/{id}")
    public String updateCompany(@PathVariable Long id, @ModelAttribute Company company, Authentication authentication) {
        User currentOwner = extractUser(authentication);
        company.setId(id);
        company.setOwner(currentOwner);
        companyService.updateCompany(company);
        return "redirect:/companies";
    }

    @PostMapping("/{id}/delete")
    public String deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return "redirect:/companies";
    }
}