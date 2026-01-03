package com.example.mechanicaldesignmanager.database;

import com.example.mechanicaldesignmanager.TaskWorkLog;
import org.springframework.data.repository.CrudRepository;

public interface TaskWorkLogRepository extends CrudRepository<TaskWorkLog,Long> {
}
