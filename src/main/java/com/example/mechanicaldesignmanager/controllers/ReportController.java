package com.example.mechanicaldesignmanager.controllers;

import com.example.mechanicaldesignmanager.AddNewUserReportForm;
import com.example.mechanicaldesignmanager.User;
import com.example.mechanicaldesignmanager.database.UserRepository;
import com.example.mechanicaldesignmanager.reports.UserReport;
import com.example.mechanicaldesignmanager.service.UserReportService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReportController {
    private UserRepository userRepo;
    private UserReportService userReportService;

    public ReportController(UserRepository userRepo, UserReportService userReportService) {
        this.userRepo = userRepo;
        this.userReportService = userReportService;
    }

    @GetMapping("/my-profile/my_report")
    public String myReportPage(@AuthenticationPrincipal User user, Model model)
    {
        /*User user = userRepo.findById(userId)
                        .orElseThrow(() ->new IllegalArgumentException("User not found: " + userId));*/

        model.addAttribute("user",user);

        return "create_my_report";
    }

    @PostMapping("/my-profile/my_report")
    public String processMyReport(@AuthenticationPrincipal User user, AddNewUserReportForm form, Model model)
    {
        /*User user = userRepo.findById(userId)
                .orElseThrow(() ->new IllegalArgumentException("User not found: " + userId));*/

        UserReport userReport = userReportService.generateUserReport(form.getStartDate(), form.getEndDate(),user);

        //model.addAttribute("user",user);
        model.addAttribute("userReport",userReport);

        return "my_report";
    }

}
