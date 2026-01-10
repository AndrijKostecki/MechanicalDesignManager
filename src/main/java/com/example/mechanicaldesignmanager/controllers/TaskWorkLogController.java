package com.example.mechanicaldesignmanager.controllers;

import com.example.mechanicaldesignmanager.*;
import com.example.mechanicaldesignmanager.database.*;
import com.example.mechanicaldesignmanager.service.TaskWorkLogService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class TaskWorkLogController {
    private UserRepository userRepo;
    private TaskRepository taskRepo;
    private ProjectRepository projectRepo;
    private UnitRepository unitRepo;
    private TaskWorkLogRepository taskWorkLogRepo;
    private TaskWorkLogService taskWorkLogService;

    public TaskWorkLogController (UserRepository userRepo, TaskRepository taskRepo,
                                  ProjectRepository projectRepo, UnitRepository unitRepo ,TaskWorkLogRepository taskWorkLogRepo,  TaskWorkLogService taskWorkLogService) {
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
        this.unitRepo = unitRepo;
        this.taskWorkLogRepo = taskWorkLogRepo;
        this.taskWorkLogService = taskWorkLogService;
    }

    @GetMapping("/project/{projectId}/unit/{unitId}/task/{taskId}/taskworklog")
    public String addTaskWorkLog(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable Long projectId,
            @PathVariable Long unitId,
            @PathVariable Long taskId,
            Model model) {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        Unit unit = task.getUnit();
        Project project = unit.getProject();

        boolean isAdmin = loggedUser.getRole() == Role.ADMIN;
        boolean isAssignedUser =
                task.getUser() != null &&
                        task.getUser().getId() == loggedUser.getId();

        boolean isCompleted = task.getStatus() == Task.taskStatus.COMPLETED;

        model.addAttribute("task", task);
        model.addAttribute("unit", unit);
        model.addAttribute("project", project);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("projectUsers", project.getUsers());
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isAssignedUser", isAssignedUser);
        model.addAttribute("isCompleted", isCompleted);

        return "new_taskworklog";
    }





    @PostMapping("/project/{projectId}/unit/{unitId}/task/{taskId}/taskworklog")
    public String processAddTaskWorkLog(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable Long projectId,
            @PathVariable Long unitId,
            @PathVariable Long taskId,
            AddNewTaskWorkLogForm form,
            RedirectAttributes redirectAttributes) {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        boolean isAdmin = loggedUser.getRole() == Role.ADMIN;
        boolean isAssignedUser =
                task.getUser() != null &&
                        task.getUser().getId() == loggedUser.getId();

        if (task.getStatus() == Task.taskStatus.COMPLETED) {
            redirectAttributes.addFlashAttribute(
                    "error", "Task is completed. You cannot report hours.");
            return "redirect:/project/" + projectId + "/unit/" + unitId;
        }

        if (!isAdmin && !isAssignedUser) {
            redirectAttributes.addFlashAttribute(
                    "error", "You are not assigned to this task.");
            return "redirect:/project/" + projectId + "/unit/" + unitId;
        }

        User workLogUser = isAdmin
                ? userRepo.findById(form.getUserId()).orElseThrow()
                : loggedUser;

        TaskWorkLog log = new TaskWorkLog(
                task,
                workLogUser,
                form.getWorkDate(),
                form.getHours()
        );

        taskWorkLogRepo.save(log);

        return "redirect:/project/" + projectId + "/unit/" + unitId;
    }

    @GetMapping("/project/{projectId}/unit/{unitId}/task/{taskId}/show_workLog")
    public String showTaskWorkLog(
            @PathVariable Long projectId,
            @PathVariable Long unitId,
            @PathVariable Long taskId,
            Model model) {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        Unit unit = task.getUnit();
        Project project = unit.getProject();

        List<TaskWorkLog> workLogList = task.getTaskWorkLogs();

        BigDecimal workHours = taskWorkLogService.calculateTaskTotalHours(task);

        model.addAttribute("task", task);
        model.addAttribute("unit", unit);
        model.addAttribute("project", project);
        model.addAttribute("workLogList", workLogList);
        model.addAttribute("workHours", workHours);

        return "task_work_log";
    }

}
