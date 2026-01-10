package com.example.mechanicaldesignmanager.database;

import com.example.mechanicaldesignmanager.TaskWorkLog;
import com.example.mechanicaldesignmanager.User;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskWorkLogRepository extends CrudRepository<TaskWorkLog,Long> {

    List<TaskWorkLog> findByUserAndWorkDateBetween(
            User user,
            LocalDate startDate,
            LocalDate endDate
    );

}
