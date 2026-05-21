package com.smallbusiness.crm.repository;

import com.smallbusiness.crm.entity.Activity;
import com.smallbusiness.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByUserOrderByCreatedAtDesc(User user);
    List<Activity> findByContactIdOrderByCreatedAtDesc(Long contactId);
    List<Activity> findByDealIdOrderByCreatedAtDesc(Long dealId);
    
    @Query("SELECT a FROM Activity a WHERE a.user = :user AND a.createdAt BETWEEN :startDate AND :endDate ORDER BY a.createdAt DESC")
    List<Activity> findActivitiesForDateRange(@Param("user") User user, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
