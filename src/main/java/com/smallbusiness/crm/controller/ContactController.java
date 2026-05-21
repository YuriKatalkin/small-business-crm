package com.smallbusiness.crm.controller;

import com.smallbusiness.crm.entity.Contact;
import com.smallbusiness.crm.service.ContactService;
import com.smallbusiness.crm.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Controller
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;
    private final CompanyService companyService;

    @GetMapping
    public String listContacts(Model model, @RequestParam(required = false) String search, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("contacts", contactService.searchContacts(search, null)); // TODO: get current user
        model.addAttribute("search", search);
        return "contacts/list";
    }

    @GetMapping("/new")
    public String createContactForm(Model model, Authentication authentication) {
        model.addAttribute("contact", new Contact());
        model.addAttribute("companies", companyService.getAllCompanies(null)); // TODO: get current user
        return "contacts/form";
    }

    @PostMapping
    public String saveContact(@ModelAttribute Contact contact, Authentication authentication) {
        // TODO: set owner and company
        contactService.createContact(contact);
        return "redirect:/contacts";
    }

    @GetMapping("/{id}/edit")
    public String editContactForm(@PathVariable Long id, Model model, Authentication authentication) {
        // TODO: get contact by id and owner
        model.addAttribute("contact", new Contact());
        model.addAttribute("companies", companyService.getAllCompanies(null));
        return "contacts/form";
    }

    @PostMapping("/{id}")
    public String updateContact(@PathVariable Long id, @ModelAttribute Contact contact, Authentication authentication) {
        // TODO: update contact
        contactService.updateContact(contact);
        return "redirect:/contacts";
    }

    @PostMapping("/{id}/delete")
    public String deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return "redirect:/contacts";
    }
}
