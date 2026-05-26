package com.smallbusiness.crm.controller;

import com.smallbusiness.crm.entity.*;
import com.smallbusiness.crm.security.CustomUserDetails;
import com.smallbusiness.crm.service.*;
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
        return ((CustomUserDetails) authentication.getPrincipal()).getUser();
    }

    @GetMapping
    public String listDeals(Model model, @RequestParam(required = false) String stage, Authentication authentication) {
        User user = extractUser(authentication);
        if (stage != null && !stage.isEmpty()) {
            model.addAttribute("deals", dealService.getDealsByStage(Deal.DealStage.valueOf(stage), user));
        } else {
            model.addAttribute("deals", dealService.getAllDeals(user));
        }
        model.addAttribute("stages", Deal.DealStage.values());
        model.addAttribute("selectedStage", stage);
        return "deals/list";
    }

    @GetMapping("/new")
    public String createDealForm(Model model, Authentication authentication) {
        User user = extractUser(authentication);
        model.addAttribute("deal", new Deal());
        model.addAttribute("contacts", contactService.getAllContacts(user));
        model.addAttribute("companies", companyService.getAllCompanies(user));
        model.addAttribute("stages", Deal.DealStage.values());
        model.addAttribute("statuses", Deal.DealStatus.values());
        return "deals/form";
    }

    @PostMapping
    public String saveDeal(@ModelAttribute Deal deal,
                           @RequestParam Long contactId,
                           @RequestParam(required = false) Long companyId,
                           Authentication authentication) {
        User user = extractUser(authentication);
        deal.setOwner(user);
        contactService.getContactById(contactId, user).ifPresent(deal::setContact);
        if (companyId != null) {
            companyService.getCompanyById(companyId, user).ifPresent(deal::setCompany);
        }
        dealService.createDeal(deal);
        return "redirect:/deals";
    }

    @GetMapping("/{id}/edit")
    public String editDealForm(@PathVariable Long id, Model model, Authentication authentication) {
        User user = extractUser(authentication);
        Deal deal = dealService.getDealById(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Сделка не найдена"));
        model.addAttribute("deal", deal);
        model.addAttribute("contacts", contactService.getAllContacts(user));
        model.addAttribute("companies", companyService.getAllCompanies(user));
        model.addAttribute("stages", Deal.DealStage.values());
        model.addAttribute("statuses", Deal.DealStatus.values());
        return "deals/form";
    }

    @PostMapping("/{id}")
    public String updateDeal(@PathVariable Long id,
                             @ModelAttribute Deal deal,
                             @RequestParam Long contactId,
                             @RequestParam(required = false) Long companyId,
                             Authentication authentication) {
        User user = extractUser(authentication);
        deal.setId(id);
        deal.setOwner(user);
        contactService.getContactById(contactId, user).ifPresent(deal::setContact);
        if (companyId != null) {
            companyService.getCompanyById(companyId, user).ifPresent(deal::setCompany);
        }
        dealService.updateDeal(deal);
        return "redirect:/deals";
    }

    @PostMapping("/{id}/delete")
    public String deleteDeal(@PathVariable Long id) {
        dealService.deleteDeal(id);
        return "redirect:/deals";
    }
}