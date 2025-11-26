package com.example.mechanicaldesignmanager;

import lombok.Data;


@Data
public class AddNewTaskForm {
    private String taskName;
    private String description;
    private Long userId;
    private Long unitId;
}
