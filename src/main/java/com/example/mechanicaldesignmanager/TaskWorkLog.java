package com.example.mechanicaldesignmanager;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "work_logs")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class TaskWorkLog {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Task task;

    @ManyToOne
    private User user;

    private LocalDate workDate;
    private BigDecimal hours;

    public TaskWorkLog(Task task, User user, LocalDate workDate, BigDecimal hours) {

        this.task = task;
        this.user = user;
        this.workDate = workDate;
        this.hours = hours;
    }
}
