package com.example.mechanicaldesignmanager.controllers;

import com.example.mechanicaldesignmanager.*;
import com.example.mechanicaldesignmanager.database.ProjectRepository;
import com.example.mechanicaldesignmanager.database.TaskRepository;
import com.example.mechanicaldesignmanager.database.UnitRepository;
import com.example.mechanicaldesignmanager.database.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class TaskController {
    private UnitRepository unitRepo;
    private TaskRepository taskRepo;
    private UserRepository userRepo;

    public TaskController(UnitRepository unitRepo, TaskRepository taskRepo,  UserRepository userRepo) {
        this.unitRepo = unitRepo;
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/project/{projectId}/unit/{id}/task/new")
    public String AddNewTaskForm (@PathVariable Long projectId,@PathVariable("id") Long unitId, Model model) {
        Unit unit = unitRepo.findById(unitId)
                .orElseThrow(() -> new IllegalArgumentException("Unit not found: " + unitId));
        model.addAttribute("unit", unit);
        List<User> projectUsers = new ArrayList<>(unit.getProject().getUsers());
        model.addAttribute("projectUsers", projectUsers);
        model.addAttribute("project", unit.getProject());
        return "new_task";
    }

    @PostMapping("/project/{projectId}/unit/{unitId}/task/new")
    public String processAddNewTask(
            @PathVariable Long projectId,
            @PathVariable Long unitId,
            AddNewTaskForm form) {

        Unit unit = unitRepo.findById(unitId)
                .orElseThrow(() -> new IllegalArgumentException("Unit not found: " + unitId));

        User user = userRepo.findById(form.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + form.getUserId()));

        Task task = new Task();
        task.setTaskName(form.getTaskName());
        task.setDescription(form.getDescription());
        task.setUnit(unit);
        task.setUser(user);
        task.setStatus(Task.taskStatus.OPENED);

        taskRepo.save(task);
        return "redirect:/project/" + projectId + "/unit/" + unitId;
    }


    @GetMapping("/project/{projectId}/unit/{unitId}/task/{taskId}/change_status")
    public String changeTaskStatus(@PathVariable Long projectId, @PathVariable Long unitId,
            @PathVariable Long taskId, Model model) {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        List<Task.taskStatus> statuses = Arrays.asList(Task.taskStatus.values());

        Unit unit = task.getUnit();
        Project project = unit.getProject();

        model.addAttribute("task", task);
        model.addAttribute("statuses", statuses);
        model.addAttribute("project", project);
        model.addAttribute("unit", unit);


        return "change_task_status";
    }

    @PostMapping("/project/{projectId}/unit/{unitId}/task/{taskId}/change_status")
    public String processChangeTaskStatus(
            @PathVariable Long taskId, @PathVariable Long projectId, @PathVariable Long unitId, AddNewTaskForm form) {

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        task.setStatus(form.getStatus());

        taskRepo.save(task);

        return "redirect:/project/" + projectId + "/unit/" + unitId;
    }




}
