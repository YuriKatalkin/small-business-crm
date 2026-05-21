package com.smallbusiness.crm.repository;

import com.smallbusiness.crm.entity.Task;
import com.smallbusiness.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedTo(User user);
    List<Task> findByAssignedToAndStatus(User user, Task.TaskStatus status);
    Optional<Task> findByIdAndAssignedTo(Long id, User user);
    
    @Query("SELECT t FROM Task t WHERE t.assignedTo = :user AND t.dueDate BETWEEN :startDate AND :endDate ORDER BY t.dueDate ASC")
    List<Task> findTasksForDateRange(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignedTo = :user AND t.status = 'COMPLETED'")
    Long countCompletedTasks(@Param("user") User user);
}
