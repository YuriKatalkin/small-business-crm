package com.smallbusiness.crm.repository;

import com.smallbusiness.crm.entity.Contact;
import com.smallbusiness.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByOwner(User owner);
    List<Contact> findByOwnerAndFirstNameContainingIgnoreCase(User owner, String firstName);
    List<Contact> findByOwnerAndLastNameContainingIgnoreCase(User owner, String lastName);
    Optional<Contact> findByIdAndOwner(Long id, User owner);
    
    @Query("SELECT c FROM Contact c WHERE c.owner = :owner AND (LOWER(c.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Contact> searchContacts(@Param("owner") User owner, @Param("search") String search);
}
