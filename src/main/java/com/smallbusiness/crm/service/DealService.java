package com.smallbusiness.crm.service;

import com.smallbusiness.crm.entity.Deal;
import com.smallbusiness.crm.entity.User;
import com.smallbusiness.crm.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DealService {
    private final DealRepository dealRepository;

    public Deal createDeal(Deal deal) {
        return dealRepository.save(deal);
    }

    public Optional<Deal> getDealById(Long id, User owner) {
        return dealRepository.findByIdAndOwner(id, owner);
    }

    public List<Deal> getAllDeals(User owner) {
        return dealRepository.findByOwner(owner);
    }

    public List<Deal> getDealsByStage(Deal.DealStage stage, User owner) {
        return dealRepository.findByOwnerAndStage(owner, stage);
    }

    public List<Deal> getDealsByStatus(Deal.DealStatus status, User owner) {
        return dealRepository.findByOwnerAndStatus(owner, status);
    }

    public Deal updateDeal(Deal deal) {
        return dealRepository.save(deal);
    }

    public void deleteDeal(Long id) {
        dealRepository.deleteById(id);
    }

    public BigDecimal getTotalWonAmount(User owner) {
        BigDecimal total = dealRepository.calculateTotalWonAmount(owner);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Long getClosedDealsCount(User owner) {
        Long count = dealRepository.countClosedDeals(owner);
        return count != null ? count : 0L;
    }

    public BigDecimal getPipelineAmount(User owner) {
        BigDecimal total = dealRepository.calculatePipelineAmount(owner);
        return total != null ? total : BigDecimal.ZERO;
    }
}
