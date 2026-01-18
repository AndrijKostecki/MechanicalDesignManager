package com.example.mechanicaldesignmanager;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AddNewProjectReportForm {

    private LocalDate startDate;
    private LocalDate endDate;
    private Long projectId;
}
