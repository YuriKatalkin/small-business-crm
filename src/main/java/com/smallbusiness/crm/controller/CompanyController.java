package com.smallbusiness.crm.controller;

import com.smallbusiness.crm.entity.Company;
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

    @GetMapping
    public String listCompanies(Model model, @RequestParam(required = false) String search, Authentication authentication) {
        model.addAttribute("companies", companyService.searchCompanies(search, null)); // TODO: get current user
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
        // TODO: set owner
        companyService.createCompany(company);
        return "redirect:/companies";
    }

    @GetMapping("/{id}/edit")
    public String editCompanyForm(@PathVariable Long id, Model model, Authentication authentication) {
        // TODO: get company by id
        model.addAttribute("company", new Company());
        return "companies/form";
    }

    @PostMapping("/{id}")
    public String updateCompany(@PathVariable Long id, @ModelAttribute Company company, Authentication authentication) {
        companyService.updateCompany(company);
        return "redirect:/companies";
    }

    @PostMapping("/{id}/delete")
    public String deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return "redirect:/companies";
    }
}
