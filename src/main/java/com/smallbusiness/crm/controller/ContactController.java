package com.smallbusiness.crm.controller;

import com.smallbusiness.crm.entity.Contact;
import com.smallbusiness.crm.entity.User;
import com.smallbusiness.crm.security.CustomUserDetails;
import com.smallbusiness.crm.service.CompanyService;
import com.smallbusiness.crm.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;
    private final CompanyService companyService;

    private User extractUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

    @GetMapping
    public String listContacts(Model model, @RequestParam(required = false) String search, Authentication authentication) {
        User currentOwner = extractUser(authentication);
        // Исправлено: аргументы приведены к порядку (searchTerm, owner) как в сервисе
        model.addAttribute("contacts", contactService.searchContacts(search, currentOwner));
        model.addAttribute("search", search);
        return "contacts/list";
    }

    @GetMapping("/new")
    public String createContactForm(Model model, Authentication authentication) {
        User currentOwner = extractUser(authentication);
        model.addAttribute("contact", new Contact());
        model.addAttribute("companies", companyService.getAllCompanies(currentOwner));
        return "contacts/form";
    }

    @PostMapping
    public String saveContact(@ModelAttribute Contact contact, Authentication authentication) {
        User currentOwner = extractUser(authentication);
        contact.setOwner(currentOwner);
        contactService.createContact(contact);
        return "redirect:/contacts";
    }

    @GetMapping("/{id}/edit")
    public String editContactForm(@PathVariable Long id, Model model, Authentication authentication) {
        User currentOwner = extractUser(authentication);
        // Исправлено: имя метода заменено на getContactById в соответствии с ContactService
        Contact contact = contactService.getContactById(id, currentOwner)
                .orElseThrow(() -> new IllegalArgumentException("Контакт не найден"));
        model.addAttribute("contact", contact);
        model.addAttribute("companies", companyService.getAllCompanies(currentOwner));
        return "contacts/form";
    }

    @PostMapping("/{id}")
    public String updateContact(@PathVariable Long id, @ModelAttribute Contact contact, Authentication authentication) {
        User currentOwner = extractUser(authentication);
        contact.setId(id);
        contact.setOwner(currentOwner);
        contactService.updateContact(contact);
        return "redirect:/contacts";
    }

    @PostMapping("/{id}/delete")
    public String deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return "redirect:/contacts";
    }
}