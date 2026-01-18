package com.example.mechanicaldesignmanager.reports;


import com.example.mechanicaldesignmanager.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class ProjectReport {
    private LocalDate startDate;
    private LocalDate endDate;
    private Project project;
    private Map<Unit, List<TaskWorkLog>> logsGroupedByUnit;
    private Map<Unit, BigDecimal> totalByUnit;
    private BigDecimal totalProjectHours;


    public ProjectReport(LocalDate startDate, LocalDate endDate, Project project,
                         Map<Unit, List<TaskWorkLog>> LogsGroupedByUnit, Map<Unit, BigDecimal> totalByUnit,
                         BigDecimal totalProjectHours) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.project = project;
        this.logsGroupedByUnit = LogsGroupedByUnit;
        this.totalByUnit = totalByUnit;
        this.totalProjectHours = totalProjectHours;

    }

}
