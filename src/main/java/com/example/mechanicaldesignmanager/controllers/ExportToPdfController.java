package com.example.mechanicaldesignmanager.controllers;

import com.example.mechanicaldesignmanager.Project;
import com.example.mechanicaldesignmanager.User;
import com.example.mechanicaldesignmanager.database.ProjectRepository;
import com.example.mechanicaldesignmanager.database.UserRepository;
import com.example.mechanicaldesignmanager.reports.ProjectReport;
import com.example.mechanicaldesignmanager.reports.UserReport;
import com.example.mechanicaldesignmanager.service.*;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
@Data
public class ExportToPdfController {
    private UserRepository userRepo;
    private UserReportService userReportService;
    private ProjectRepository projectRepo;
    private PdfGeneratorService pdfGeneratorService;
    private ProjectReportService projectReportService;

    public ExportToPdfController(UserRepository userRepo, UserReportService userReportService, ProjectRepository projectRepo, ProjectReportService projectReportService, PdfGeneratorService pdfGeneratorService) {
        this.userRepo = userRepo;
        this.userReportService = userReportService;
        this.projectReportService = projectReportService;
        this.projectRepo = projectRepo;
        this.pdfGeneratorService = pdfGeneratorService;
    }


    @GetMapping("/my-profile/my_report/pdf")
    public ResponseEntity<byte[]> exportMyReportPdf(
            @AuthenticationPrincipal User user,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        UserReport report =
                userReportService.generateUserReport(startDate, endDate, user);

        Map<String, Object> data = new HashMap<>();
        data.put("userReport", report);

        byte[] pdf = pdfGeneratorService.generatePdf("my_report_pdf", data);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=my_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/admin_tools/project_report/pdf")
    public ResponseEntity<byte[]> exportProjectReportPdf(
            @RequestParam Long projectId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Project not found"));
        ProjectReport projectReport =
                projectReportService.generateProjectReport(startDate, endDate, project);

        Map<String, Object> data = new HashMap<>();
        data.put("projectReport", projectReport);

        byte[] pdf = pdfGeneratorService.generatePdf("project_report_pdf", data);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=project_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }


}
