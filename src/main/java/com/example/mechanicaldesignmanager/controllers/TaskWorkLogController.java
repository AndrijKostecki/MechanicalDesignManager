package com.example.mechanicaldesignmanager.controllers;

import com.example.mechanicaldesignmanager.*;
import com.example.mechanicaldesignmanager.database.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class TaskWorkLogController {
    private UserRepository userRepo;
    private TaskRepository taskRepo;
    private ProjectRepository projectRepo;
    private UnitRepository unitRepo;
    private TaskWorkLogRepository taskWorkLogRepo;

    public TaskWorkLogController (UserRepository userRepo, TaskRepository taskRepo,
                                  ProjectRepository projectRepo, UnitRepository unitRepo ,TaskWorkLogRepository taskWorkLogRepo) {
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
        this.unitRepo = unitRepo;
        this.taskWorkLogRepo = taskWorkLogRepo;
    }

    @GetMapping("/{projectId}/unit/{unitId}/task/{taskId}/taskworklog")
    public String addTaskWorkLog(@AuthenticationPrincipal Long userId, @PathVariable Long projectId,
                                 @PathVariable Long unitId, @PathVariable Long taskId, Model model) {

        User user = userRepo.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found: " + userId));
        Task task = taskRepo.findById(taskId).orElseThrow(() ->
                new IllegalArgumentException("Task not found: " + taskId));
        Project project = task.getUnit().getProject();
        Unit unit = task.getUnit();

        List<User> projectUsers = project.getUsers();

        model.addAttribute("projectUsers", projectUsers);
        model.addAttribute("project", project);
        model.addAttribute("user", user);
        model.addAttribute("task", task);
        model.addAttribute("unit", unit);

        return "newTaskWorkLog";

    }

    @PostMapping("/{projectId}/unit/{unitId}/task/{taskId}/taskworklog")

    public String processAddTaskWorkLog (@PathVariable Long projectId, @PathVariable Long unitId,
                                         @PathVariable Long taskId, AddNewTaskWorkLogFrom form) {

        TaskWorkLog taskWorkLog = form.toTaskWorkLog();
        taskWorkLogRepo.save(taskWorkLog);

        return "redirect:/project/" + projectId + "/unit/" + unitId;
    }
}
