package com.smallbusiness.crm.controller;

import com.smallbusiness.crm.entity.Deal;
import com.smallbusiness.crm.service.DealService;
import com.smallbusiness.crm.service.ContactService;
import com.smallbusiness.crm.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/deals")
@RequiredArgsConstructor
public class DealController {
    private final DealService dealService;
    private final ContactService contactService;
    private final CompanyService companyService;

    @GetMapping
    public String listDeals(Model model, @RequestParam(required = false) String stage, Authentication authentication) {
        // TODO: get current user
        model.addAttribute("deals", dealService.getAllDeals(null));
        model.addAttribute("stages", Deal.DealStage.values());
        model.addAttribute("selectedStage", stage);
        return "deals/list";
    }

    @GetMapping("/new")
    public String createDealForm(Model model, Authentication authentication) {
        model.addAttribute("deal", new Deal());
        model.addAttribute("contacts", contactService.getAllContacts(null));
        model.addAttribute("companies", companyService.getAllCompanies(null));
        model.addAttribute("stages", Deal.DealStage.values());
        return "deals/form";
    }

    @PostMapping
    public String saveDeal(@ModelAttribute Deal deal, Authentication authentication) {
        // TODO: set owner
        dealService.createDeal(deal);
        return "redirect:/deals";
    }

    @GetMapping("/{id}/edit")
    public String editDealForm(@PathVariable Long id, Model model, Authentication authentication) {
        model.addAttribute("deal", new Deal());
        model.addAttribute("contacts", contactService.getAllContacts(null));
        model.addAttribute("companies", companyService.getAllCompanies(null));
        model.addAttribute("stages", Deal.DealStage.values());
        return "deals/form";
    }

    @PostMapping("/{id}")
    public String updateDeal(@PathVariable Long id, @ModelAttribute Deal deal, Authentication authentication) {
        dealService.updateDeal(deal);
        return "redirect:/deals";
    }

    @PostMapping("/{id}/delete")
    public String deleteDeal(@PathVariable Long id) {
        dealService.deleteDeal(id);
        return "redirect:/deals";
    }
}
