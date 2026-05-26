package com.smallbusiness.crm.controller;

import com.smallbusiness.crm.entity.Task;
import com.smallbusiness.crm.entity.User;
import com.smallbusiness.crm.repository.UserRepository;
import com.smallbusiness.crm.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    @GetMapping("/new")
    public String createTaskForm(Model model) {
        Task task = new Task();
        task.setStatus(Task.TaskStatus.NOT_STARTED);
        model.addAttribute("task", task);
        return "tasks/form";
    }

    @GetMapping("/{id}/edit")
    public String editTaskForm(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Optional<Task> taskOpt = taskService.getTaskById(id, currentUser);
        if (taskOpt.isEmpty()) {
            return "redirect:/dashboard";
        }

        model.addAttribute("task", taskOpt.get());
        return "tasks/form";
    }

    @PostMapping
    public String saveTask(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute Task task) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        task.setAssignedTo(currentUser);
        task.setCreatedBy(currentUser);

        if (task.getStatus() == null) task.setStatus(Task.TaskStatus.NOT_STARTED);
        if (task.getType() == null) task.setType(Task.TaskType.OTHER);
        if (task.getPriority() == null) task.setPriority(Task.TaskPriority.MEDIUM);

        taskService.saveTask(task);

        return "redirect:/dashboard";
    }

    // НОВЫЙ МЕТОД: Удаление задачи
    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Проверяем, существует ли задача и принадлежит ли она текущему пользователю
        Optional<Task> taskOpt = taskService.getTaskById(id, currentUser);
        if (taskOpt.isPresent()) {
            taskService.deleteTask(id); // Используем твой метод удаления из TaskService
        }

        return "redirect:/dashboard";
    }
}