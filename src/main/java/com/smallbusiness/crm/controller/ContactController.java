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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;
    private final CompanyService companyService;

    private User extractUser(Authentication authentication) {
        return ((CustomUserDetails) authentication.getPrincipal()).getUser();
    }

    @GetMapping
    public String listContacts(Model model, @RequestParam(required = false) String search, Authentication authentication) {
        model.addAttribute("contacts", contactService.searchContacts(search, extractUser(authentication)));
        model.addAttribute("search", search);
        return "contacts/list";
    }

    @GetMapping("/new")
    public String createContactForm(Model model,
                                    @RequestParam(required = false) String redirect,
                                    Authentication authentication) {
        Contact defaultContact = new Contact();
        defaultContact.setStatus(Contact.ContactStatus.ACTIVE);
        defaultContact.setSource(Contact.ContactSource.OTHER);

        model.addAttribute("contact", defaultContact);
        model.addAttribute("companies", companyService.getAllCompanies(extractUser(authentication)));
        model.addAttribute("redirectUrl", redirect);
        return "contacts/form";
    }

    @PostMapping
    public String saveContact(@ModelAttribute Contact contact,
                              @RequestParam(required = false) Long companyId,
                              @RequestParam(required = false) String redirect,
                              Authentication authentication) {
        User user = extractUser(authentication);
        contact.setOwner(user);

        if (companyId != null) {
            companyService.getCompanyById(companyId, user).ifPresent(contact::setCompany);
        } else {
            contact.setCompany(null);
        }

        contactService.createContact(contact);

        if (redirect != null && !redirect.isEmpty()) {
            return "redirect:" + redirect;
        }
        return "redirect:/contacts";
    }

    @GetMapping("/{id}")
    public String viewContactForm(@PathVariable Long id,
                                  Model model,
                                  @RequestParam(required = false) String redirect,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        User user = extractUser(authentication);
        Optional<Contact> contactOpt = contactService.getContactById(id, user);

        if (contactOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Контакт не найден или у вас нет к нему доступа.");
            return "redirect:/contacts";
        }

        model.addAttribute("contact", contactOpt.get());
        model.addAttribute("companies", companyService.getAllCompanies(user));
        model.addAttribute("redirectUrl", redirect);
        return "contacts/form";
    }

    @GetMapping("/{id}/edit")
    public String editContactForm(@PathVariable Long id,
                                  Model model,
                                  @RequestParam(required = false) String redirect,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        User user = extractUser(authentication);
        Optional<Contact> contactOpt = contactService.getContactById(id, user);

        if (contactOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Контакт не найден или у вас нет к нему доступа.");
            return "redirect:/contacts";
        }

        model.addAttribute("contact", contactOpt.get());
        model.addAttribute("companies", companyService.getAllCompanies(user));
        model.addAttribute("redirectUrl", redirect);
        return "contacts/form";
    }

    @PostMapping("/{id}")
    public String updateContact(@PathVariable Long id,
                                @ModelAttribute Contact contact,
                                @RequestParam(required = false) Long companyId,
                                @RequestParam(required = false) String redirect,
                                Authentication authentication) {
        User user = extractUser(authentication);
        contact.setId(id);
        contact.setOwner(user);

        if (companyId != null) {
            companyService.getCompanyById(companyId, user).ifPresent(contact::setCompany);
        } else {
            contact.setCompany(null);
        }

        contactService.updateContact(contact);

        if (redirect != null && !redirect.isEmpty()) {
            return "redirect:" + redirect;
        }
        return "redirect:/contacts";
    }

    @PostMapping("/{id}/delete")
    public String deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return "redirect:/contacts";
    }
}