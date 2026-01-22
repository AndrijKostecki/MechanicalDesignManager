package com.example.mechanicaldesignmanager;

import com.example.mechanicaldesignmanager.database.TaskWorkLogRepository;
import com.example.mechanicaldesignmanager.reports.UserReport;
import com.example.mechanicaldesignmanager.service.UserReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserReportServiceTest {

    @Mock
    private TaskWorkLogRepository taskWorkLogRepository;

    @InjectMocks
    private UserReportService userReportService;

    private User user;
    private Task task1;
    private Task task2;
    private Project project;
    private Unit unit1;
    private Unit unit2;

    @BeforeEach
    void setUp() {
        user = new User(
                "jan", "pass", "Jan", "Kowalski",
                "jan@test.com",
                LocalDate.of(1990, 1, 1),
                LocalDate.of(2025, 12, 31),
                Role.USER
        );

        project = new Project("Project A", "Desc",
                LocalDate.now(), LocalDate.now().plusDays(30));

        unit1 = new Unit("Unit 1", "Desc", project);
        unit2 = new Unit("Unit 2", "Desc", project);

        task1 = new Task();
        task1.setId(1L);
        task1.setTaskName("Task 1");
        task1.setUnit(unit1);

        task2 = new Task();
        task2.setId(2L);
        task2.setTaskName("Task 2");
        task2.setUnit(unit2);
    }




    @Test
    void shouldReturnEmptyReportWhenRepositoryReturnsNoLogs() {

        when(taskWorkLogRepository.findByUserAndWorkDateBetween(
                any(), any(), any()))
                .thenReturn(List.of());

        UserReport report = userReportService.generateUserReport(
                LocalDate.now().minusDays(10),
                LocalDate.now(),
                user
        );

        assertTrue(report.getTotalTaskHours().isEmpty());
    }


    @Test
    void shouldSumHoursForSingleTask() {
        TaskWorkLog log1 = new TaskWorkLog(task1, user,
                LocalDate.now(), BigDecimal.valueOf(5));

        TaskWorkLog log2 = new TaskWorkLog(task1, user,
                LocalDate.now().plusDays(1), BigDecimal.valueOf(3));

        when(taskWorkLogRepository.findByUserAndWorkDateBetween(
                any(), any(), any()))
                .thenReturn(List.of(log1, log2));

        UserReport report = userReportService.generateUserReport(
                LocalDate.now().minusDays(10),
                LocalDate.now().plusDays(10),
                user
        );

        assertEquals(
                BigDecimal.valueOf(8),
                report.getTotalTaskHours().get(1L)
        );
    }

    @Test
    void shouldSumHoursForMultipleTasks() {
        TaskWorkLog log1 = new TaskWorkLog(task1, user,
                LocalDate.now(), BigDecimal.valueOf(2));

        TaskWorkLog log2 = new TaskWorkLog(task2, user,
                LocalDate.now(), BigDecimal.valueOf(4));

        when(taskWorkLogRepository.findByUserAndWorkDateBetween(
                any(), any(), any()))
                .thenReturn(List.of(log1, log2));

        UserReport report = userReportService.generateUserReport(
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(5),
                user
        );

        assertEquals(BigDecimal.valueOf(2), report.getTotalTaskHours().get(1L));
        assertEquals(BigDecimal.valueOf(4), report.getTotalTaskHours().get(2L));

    }
    @Test
    void shouldGroupTasksByProject() {
        TaskWorkLog log1 = new TaskWorkLog(task1, user,
                LocalDate.now(), BigDecimal.ONE);

        TaskWorkLog log2 = new TaskWorkLog(task2, user,
                LocalDate.now(), BigDecimal.ONE);

        when(taskWorkLogRepository.findByUserAndWorkDateBetween(
                any(), any(), any()))
                .thenReturn(List.of(log1, log2));

        UserReport report = userReportService.generateUserReport(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                user
        );

        assertTrue(report.getGroupedByProjectTasks().containsKey(project));
        assertEquals(2, report.getGroupedByProjectTasks().get(project).size());
    }






}
