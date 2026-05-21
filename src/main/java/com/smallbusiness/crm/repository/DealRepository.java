package com.smallbusiness.crm.repository;

import com.smallbusiness.crm.entity.Deal;
import com.smallbusiness.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {
    List<Deal> findByOwner(User owner);
    List<Deal> findByOwnerAndStage(User owner, Deal.DealStage stage);
    List<Deal> findByOwnerAndStatus(User owner, Deal.DealStatus status);
    Optional<Deal> findByIdAndOwner(Long id, User owner);
    
    @Query("SELECT SUM(d.amount) FROM Deal d WHERE d.owner = :owner AND d.stage = 'CLOSED_WON'")
    BigDecimal calculateTotalWonAmount(@Param("owner") User owner);
    
    @Query("SELECT COUNT(d) FROM Deal d WHERE d.owner = :owner AND d.stage = 'CLOSED_WON'")
    Long countClosedDeals(@Param("owner") User owner);
    
    @Query("SELECT SUM(d.amount) FROM Deal d WHERE d.owner = :owner AND d.status = 'OPEN'")
    BigDecimal calculatePipelineAmount(@Param("owner") User owner);
}
