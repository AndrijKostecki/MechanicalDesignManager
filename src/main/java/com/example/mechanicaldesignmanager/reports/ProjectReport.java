package com.example.mechanicaldesignmanager.reports;


import com.example.mechanicaldesignmanager.Project;
import com.example.mechanicaldesignmanager.Task;
import com.example.mechanicaldesignmanager.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ProjectReport {
    private LocalDate startDate;
    private LocalDate endDate;
    private User user;
    private Map<Task, BigDecimal> totalTaskHours;
    private Map<Project, List<Task>> groupedByProjectTasks;

    /*public UserReport(LocalDate startDate, LocalDate endDate, User user,
                      Map<Task, BigDecimal> totalTaskHours, Map<Project, List<Task>> groupedByProjectTasks) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.totalTaskHours = totalTaskHours;
        this.groupedByProjectTasks = groupedByProjectTasks;

    }*/

}
