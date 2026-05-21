package com.smallbusiness.crm.service;

import com.smallbusiness.crm.entity.Task;
import com.smallbusiness.crm.entity.User;
import com.smallbusiness.crm.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long id, User assignedTo) {
        return taskRepository.findByIdAndAssignedTo(id, assignedTo);
    }

    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByAssignedTo(user);
    }

    public List<Task> getTasksByStatus(Task.TaskStatus status, User user) {
        return taskRepository.findByAssignedToAndStatus(user, status);
    }

    public List<Task> getTasksByDateRange(LocalDate startDate, LocalDate endDate, User user) {
        return taskRepository.findTasksForDateRange(user, startDate, endDate);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Long getCompletedTasksCount(User user) {
        Long count = taskRepository.countCompletedTasks(user);
        return count != null ? count : 0L;
    }
}
