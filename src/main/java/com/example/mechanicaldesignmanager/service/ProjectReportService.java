package com.example.mechanicaldesignmanager.service;

import com.example.mechanicaldesignmanager.Project;
import com.example.mechanicaldesignmanager.TaskWorkLog;
import com.example.mechanicaldesignmanager.Unit;
import com.example.mechanicaldesignmanager.database.TaskWorkLogRepository;
import com.example.mechanicaldesignmanager.database.UnitRepository;
import com.example.mechanicaldesignmanager.reports.ProjectReport;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectReportService {

    private UnitRepository unitRepo;
    private TaskWorkLogRepository taskWorkLogRepo;

    public ProjectReportService(UnitRepository unitRepo, TaskWorkLogRepository taskWorkLogRepo) {
        this.unitRepo = unitRepo;
        this.taskWorkLogRepo = taskWorkLogRepo;
    }

    private Map<Unit, List<TaskWorkLog>> logsGroupedByUnit(List<TaskWorkLog> logsByDate, List<Unit> units) {

         Map<Unit, List<TaskWorkLog>> logsGroupedByUnit = new HashMap<>();

         for (TaskWorkLog log : logsByDate) {

             if(units.contains(log.getTask().getUnit()) && !logsGroupedByUnit.containsKey(log.getTask().getUnit())) {

                 logsGroupedByUnit.put(log.getTask().getUnit(), new ArrayList<>());
                 logsGroupedByUnit.get(log.getTask().getUnit()).add(log);
             }

             else if (units.contains(log.getTask().getUnit()) && logsGroupedByUnit.containsKey(log.getTask().getUnit())) {

                 logsGroupedByUnit.get(log.getTask().getUnit()).add(log);
             }


         }

         return logsGroupedByUnit;
    }

    private Map <Unit, BigDecimal> totalByUnit (Map<Unit, List<TaskWorkLog>> LogsGroupedByUnit) {

        Map<Unit, BigDecimal> totalByUnit = new HashMap<>();

        for (Unit unit : LogsGroupedByUnit.keySet()) {
            BigDecimal totalUnitHours = BigDecimal.ZERO;
            for (TaskWorkLog taskWorkLog : LogsGroupedByUnit.get(unit)) {
                totalUnitHours = totalUnitHours.add(taskWorkLog.getHours());
            }
            totalByUnit.put(unit, totalUnitHours);
        }
        return totalByUnit;
    }

    private BigDecimal totalProjectHours (Map<Unit, BigDecimal> totalByUnit) {
        BigDecimal totalHours = BigDecimal.ZERO;
        for (Unit unit : totalByUnit.keySet()) {
            totalHours = totalHours.add(totalByUnit.get(unit));
        }
        return totalHours;
    }

    public ProjectReport generateProjectReport(LocalDate startDate, LocalDate endDate, Project project) {

        List<Unit> units = unitRepo.findByProject(project);
        List<TaskWorkLog> taskWorkLogs = taskWorkLogRepo.findByWorkDateBetween(startDate, endDate);

        Map<Unit, List<TaskWorkLog>> LogsGroupedByUnit = logsGroupedByUnit(taskWorkLogs, units);

        Map<Unit, BigDecimal> totalByUnit = totalByUnit(LogsGroupedByUnit);

        BigDecimal totalProjectHours = totalProjectHours(totalByUnit);

        return new ProjectReport(startDate, endDate, project, LogsGroupedByUnit, totalByUnit, totalProjectHours);

    }




}
