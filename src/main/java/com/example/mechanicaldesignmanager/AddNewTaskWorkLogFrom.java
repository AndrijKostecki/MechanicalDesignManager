package com.example.mechanicaldesignmanager;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AddNewTaskWorkLogFrom {

    private Task task;

    private User user;

    private LocalDate workDate;
    private BigDecimal hours;

    public TaskWorkLog toTaskWorkLog () {
        return new TaskWorkLog(task, user, workDate, hours);
    }

}
