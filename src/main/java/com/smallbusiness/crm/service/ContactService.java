package com.smallbusiness.crm.service;

import com.smallbusiness.crm.entity.Contact;
import com.smallbusiness.crm.entity.User;
import com.smallbusiness.crm.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactService {
    private final ContactRepository contactRepository;

    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public Optional<Contact> getContactById(Long id, User owner) {
        return contactRepository.findByIdAndOwner(id, owner);
    }

    public List<Contact> getAllContacts(User owner) {
        return contactRepository.findByOwner(owner);
    }

    public List<Contact> searchContacts(String searchTerm, User owner) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return getAllContacts(owner);
        }
        return contactRepository.searchContacts(owner, searchTerm);
    }

    public Contact updateContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }

    public long getTotalContacts(User owner) {
        return contactRepository.findByOwner(owner).size();
    }
}
