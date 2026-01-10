package com.example.mechanicaldesignmanager;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AddNewTaskWorkLogForm {


    private Long userId;


    private LocalDate workDate;


    private BigDecimal hours;

}
