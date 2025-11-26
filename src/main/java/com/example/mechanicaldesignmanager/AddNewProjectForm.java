package com.example.mechanicaldesignmanager;

import lombok.Data;


import java.time.LocalDate;
import java.util.Date;

@Data
public class AddNewProjectForm {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    public Project toProject () {
        return new Project(
                title, description, startDate, endDate);
    }
}
