package com.example.mechanicaldesignmanager.reports;

import com.example.mechanicaldesignmanager.Project;
import com.example.mechanicaldesignmanager.Task;
import com.example.mechanicaldesignmanager.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class UserReport  {

    private LocalDate startDate;
    private LocalDate endDate;
    private User user;
    private Map<Long, BigDecimal> totalTaskHours;
    private Map<Project, List<Task>> groupedByProjectTasks;

    public UserReport(LocalDate startDate, LocalDate endDate, User user,
                      Map<Long, BigDecimal> totalTaskHours, Map<Project, List<Task>> groupedByProjectTasks) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.totalTaskHours = totalTaskHours;
        this.groupedByProjectTasks = groupedByProjectTasks;

    }





}
