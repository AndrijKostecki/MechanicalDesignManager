package com.example.mechanicaldesignmanager.service;

import com.example.mechanicaldesignmanager.Project;
import com.example.mechanicaldesignmanager.Task;
import com.example.mechanicaldesignmanager.TaskWorkLog;
import com.example.mechanicaldesignmanager.User;
import com.example.mechanicaldesignmanager.database.TaskRepository;
import com.example.mechanicaldesignmanager.database.TaskWorkLogRepository;
import com.example.mechanicaldesignmanager.database.UserRepository;
import com.example.mechanicaldesignmanager.reports.UserReport;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class UserReportService {

    private TaskWorkLogRepository taskWorkLogRepo;


    public UserReportService(TaskWorkLogRepository taskWorkLogRepo) {
        this.taskWorkLogRepo = taskWorkLogRepo;
    }


    private Map<Long, BigDecimal> sumHoursByTask (List<TaskWorkLog> logsForReport){

        Map<Long, BigDecimal> totalTaskHours = new HashMap<>();

        for (TaskWorkLog taskWorkLog : logsForReport) {
            if (!totalTaskHours.containsKey(taskWorkLog.getTask().getId())) {
                totalTaskHours.put(taskWorkLog.getTask().getId(), taskWorkLog.getHours());
            } else {
                totalTaskHours.put(taskWorkLog.getTask().getId(), totalTaskHours.get(taskWorkLog.getTask().getId()).add(taskWorkLog.getHours()));
            }
        }

        return totalTaskHours;

    }

    private Map<Project, List<Task>> groupByProject (List<TaskWorkLog> logsForReport){
        Map<Project, List<Task>> groupedByProjectTasks = new HashMap<>();

        List<Task> tasks = new ArrayList<>();
        for (TaskWorkLog log : logsForReport) {
            if (!tasks.contains(log.getTask())) {
                tasks.add(log.getTask());
            }
        }


        for (Task task : tasks){

            Project project = task.getUnit().getProject();

            if (!groupedByProjectTasks.containsKey(project)){

                groupedByProjectTasks.put(project, new ArrayList<>());
                groupedByProjectTasks.get(project).add(task);
            }
            else{ groupedByProjectTasks.get(project).add(task); }

        }

        return groupedByProjectTasks;
    }

    public UserReport generateUserReport(LocalDate startDate, LocalDate endDate, User user){

        List<TaskWorkLog> logsForReport =
                taskWorkLogRepo.findByUserAndWorkDateBetween(user, startDate, endDate);


        Map<Long, BigDecimal> totalTaskHours = sumHoursByTask(logsForReport);

        Map<Project, List<Task>> groupedByProjectTasks =  groupByProject(logsForReport);



        return new UserReport (startDate, endDate, user,
                totalTaskHours, groupedByProjectTasks);
    }





    }


