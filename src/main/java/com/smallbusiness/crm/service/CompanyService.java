package com.smallbusiness.crm.service;

import com.smallbusiness.crm.entity.Company;
import com.smallbusiness.crm.entity.User;
import com.smallbusiness.crm.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {
    private final CompanyRepository companyRepository;

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Optional<Company> getCompanyById(Long id, User owner) {
        return companyRepository.findByIdAndOwner(id, owner);
    }

    public List<Company> getAllCompanies(User owner) {
        return companyRepository.findByOwner(owner);
    }

    public List<Company> searchCompanies(String searchTerm, User owner) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return getAllCompanies(owner);
        }
        return companyRepository.searchCompanies(owner, searchTerm);
    }

    public Company updateCompany(Company company) {
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    public long getTotalCompanies(User owner) {
        return companyRepository.findByOwner(owner).size();
    }
}
