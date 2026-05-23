package com.smallbusiness.crm.controller;

import com.smallbusiness.crm.entity.Deal;
import com.smallbusiness.crm.entity.User;
import com.smallbusiness.crm.security.CustomUserDetails;
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

    private User extractUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

    @GetMapping
    public String listDeals(Model model, @RequestParam(required = false) String stage, Authentication authentication) {
        User currentOwner = extractUser(authentication);
        if (stage != null && !stage.isEmpty()) {
            // Исправлено: изменен порядок параметров на (stage, owner) согласно логике DealService
            model.addAttribute("deals", dealService.getDealsByStage(Deal.DealStage.valueOf(stage), currentOwner));
        } else {
            model.addAttribute("deals", dealService.getAllDeals(currentOwner));
        }
        model.addAttribute("stages", Deal.DealStage.values());
        model.addAttribute("selectedStage", stage);
        return "deals/list";
    }

    @GetMapping("/new")
    public String createDealForm(Model model, Authentication authentication) {
        User currentOwner = extractUser(authentication);
        model.addAttribute("deal", new Deal());
        model.addAttribute("contacts", contactService.getAllContacts(currentOwner));
        model.addAttribute("companies", companyService.getAllCompanies(currentOwner));
        model.addAttribute("stages", Deal.DealStage.values());
        return "deals/form";
    }

    @PostMapping
    public String saveDeal(@ModelAttribute Deal deal, Authentication authentication) {
        User currentOwner = extractUser(authentication);
        deal.setOwner(currentOwner);
        dealService.createDeal(deal);
        return "redirect:/deals";
    }

    @GetMapping("/{id}/edit")
    public String editDealForm(@PathVariable Long id, Model model, Authentication authentication) {
        User currentOwner = extractUser(authentication);
        // Исправлено: вызов getDealById вместо несуществующего getDealByIdAndOwner
        Deal deal = dealService.getDealById(id, currentOwner)
                .orElseThrow(() -> new IllegalArgumentException("Сделка не найдена"));
        model.addAttribute("deal", deal);
        model.addAttribute("contacts", contactService.getAllContacts(currentOwner));
        model.addAttribute("companies", companyService.getAllCompanies(currentOwner));
        model.addAttribute("stages", Deal.DealStage.values());
        return "deals/form";
    }

    @PostMapping("/{id}")
    public String updateDeal(@PathVariable Long id, @ModelAttribute Deal deal, Authentication authentication) {
        User currentOwner = extractUser(authentication);
        deal.setId(id);
        deal.setOwner(currentOwner);
        dealService.updateDeal(deal);
        return "redirect:/deals";
    }

    @PostMapping("/{id}/delete")
    public String deleteDeal(@PathVariable Long id) {
        dealService.deleteDeal(id);
        return "redirect:/deals";
    }
}