package com.example.mechanicaldesignmanager.controllers;

import com.example.mechanicaldesignmanager.AddNewProjectReportForm;
import com.example.mechanicaldesignmanager.AddNewUserReportForm;
import com.example.mechanicaldesignmanager.Project;
import com.example.mechanicaldesignmanager.User;
import com.example.mechanicaldesignmanager.database.ProjectRepository;
import com.example.mechanicaldesignmanager.database.UserRepository;
import com.example.mechanicaldesignmanager.reports.ProjectReport;
import com.example.mechanicaldesignmanager.reports.UserReport;
import com.example.mechanicaldesignmanager.service.ProjectReportService;
import com.example.mechanicaldesignmanager.service.UserReportService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ReportController {
    private UserRepository userRepo;
    private ProjectRepository projectRepo;
    private UserReportService userReportService;
    private ProjectReportService projectReportService;

    public ReportController(UserRepository userRepo, UserReportService userReportService, ProjectReportService projectReportService, ProjectRepository projectRepo) {
        this.userRepo = userRepo;
        this.userReportService = userReportService;
        this.projectRepo = projectRepo;
        this.projectReportService = projectReportService;

    }

    @GetMapping("/my-profile/my_report")
    public String myReportPage(@AuthenticationPrincipal User user, Model model)
    {
        model.addAttribute("user",user);

        return "create_my_report";
    }

    @PostMapping("/my-profile/my_report")
    public String processMyReport(@AuthenticationPrincipal User user, AddNewUserReportForm form, Model model)
    {
        UserReport userReport = userReportService.generateUserReport(form.getStartDate(), form.getEndDate(),user);

        model.addAttribute("userReport",userReport);

        return "my_report";
    }

    @GetMapping("/admin_tools/project_report/new")
    public String newProjectReport(Model model) {
        List<Project> projects = (ArrayList<Project>) projectRepo.findAll();
        model.addAttribute("projects",projects);
        return "new_project_report";
    }

    @PostMapping("/admin_tools/project_report/new")
    public String processNewProjectReport (AddNewProjectReportForm form, Model model) {

        Project project = projectRepo.findById(form.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        ProjectReport projectReport = projectReportService.generateProjectReport(form.getStartDate(), form.getEndDate(),project);

        model.addAttribute("projectReport",projectReport);
        model.addAttribute("project",project);

        return "project_report";


    }

}
