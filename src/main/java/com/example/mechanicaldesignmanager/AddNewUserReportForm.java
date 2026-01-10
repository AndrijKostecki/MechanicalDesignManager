package com.example.mechanicaldesignmanager;


import lombok.Data;

import java.time.LocalDate;

@Data
public class AddNewUserReportForm {

    private LocalDate startDate;
    private LocalDate endDate;

}



