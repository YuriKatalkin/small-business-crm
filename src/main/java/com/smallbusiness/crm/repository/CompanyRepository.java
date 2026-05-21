package com.smallbusiness.crm.repository;

import com.smallbusiness.crm.entity.Company;
import com.smallbusiness.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByOwner(User owner);
    Optional<Company> findByIdAndOwner(Long id, User owner);
    Boolean existsByRegistrationNumberAndOwner(String registrationNumber, User owner);
    
    @Query("SELECT c FROM Company c WHERE c.owner = :owner AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(c.industry) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Company> searchCompanies(@Param("owner") User owner, @Param("search") String search);
}
