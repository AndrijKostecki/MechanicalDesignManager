package com.example.mechanicaldesignmanager.controllers;

import com.example.mechanicaldesignmanager.User;
import com.example.mechanicaldesignmanager.database.UserRepository;
import com.example.mechanicaldesignmanager.reports.UserReport;
import com.example.mechanicaldesignmanager.service.PdfGeneratorService;
import com.example.mechanicaldesignmanager.service.UserReportService;
import com.example.mechanicaldesignmanager.service.UserService;
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
    private PdfGeneratorService pdfGeneratorService;

    public ExportToPdfController(UserRepository userRepo, UserReportService userReportService, PdfGeneratorService pdfGeneratorService) {
        this.userRepo = userRepo;
        this.userReportService = userReportService;
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


}
