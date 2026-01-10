package com.example.mechanicaldesignmanager.controllers;

import com.example.mechanicaldesignmanager.AddNewUnitForm;
import com.example.mechanicaldesignmanager.Project;
import com.example.mechanicaldesignmanager.Task;
import com.example.mechanicaldesignmanager.Unit;
import com.example.mechanicaldesignmanager.database.ProjectRepository;
import com.example.mechanicaldesignmanager.database.UnitRepository;
import com.example.mechanicaldesignmanager.service.TaskWorkLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("project")
public class UnitController {

    private UnitRepository unitRepo;
    private ProjectRepository projectRepo;
    private TaskWorkLogService taskWorkLogService;
    public UnitController(UnitRepository unitRepository, ProjectRepository projectRepo, TaskWorkLogService taskWorkLogService) {
        this.unitRepo= unitRepository;
        this.projectRepo = projectRepo;
        this.taskWorkLogService = taskWorkLogService;
    }

    @GetMapping("/{projectId}/unit/new")
    public String AddNewUnitForm (@PathVariable Long projectId, Model model) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));
        model.addAttribute("project", project);
        return "new_unit";
    }

    @PostMapping("/{projectId}/unit/new")
    public String processAddNewUnit (@PathVariable Long projectId, AddNewUnitForm form) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));

        Unit unit = form.toUnit();
        unit.setProject(project);
        unitRepo.save(unit);

        return "redirect:/project/" + projectId;
    }


    @GetMapping("/{projectId}/unit/{id}")
    public String unitPage(@PathVariable Long projectId,
                           @PathVariable Long id,
                           Model model) {
        Unit unit = unitRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Unit not found: " + id));
        Project project = unit.getProject();

        Map<Long, BigDecimal> taskHoursMap = new HashMap<>();

        for (Task task : unit.getTasks()) {
            BigDecimal hours = taskWorkLogService.calculateTaskTotalHours(task);
            taskHoursMap.put(task.getId(), hours);
        }

        model.addAttribute("unit", unit);
        model.addAttribute("project", project);
        model.addAttribute("taskHoursMap", taskHoursMap);
        return "unit";
    }


    @GetMapping("/{projectId}/unit/{id}/edit_unit")
    public String editUnit(@PathVariable Long projectId,
                           @PathVariable Long id,
                           Model model) {
        Unit unit = unitRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Unit not found: " + id));

        Project project = unit.getProject();
        model.addAttribute("unit", unit);
        model.addAttribute("project", project);

        return "edit_unit";
    }

    @PostMapping("/{projectId}/unit/{id}/edit_unit")

    public String processEditUnit (@PathVariable Long projectId,
                                    @PathVariable Long id,
                                    AddNewUnitForm form) {
        Unit unit = unitRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Unit not found: " + id));

        unit.setTitle(form.getTitle());
        unit.setDescription(form.getDescription());
        unitRepo.save(unit);

        return "redirect:/project/" + projectId + "/unit/" + id;
    }

    @PostMapping ("/{projectId}/unit/{id}/delete_unit")
    public String deleteUnit (@PathVariable Long projectId, @PathVariable Long id){

        Unit unit = unitRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Unit not found: " + id));

        unitRepo.delete(unit);


        return "redirect:/project/" + projectId;
    }






}
