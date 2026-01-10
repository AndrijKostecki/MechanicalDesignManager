package com.example.mechanicaldesignmanager.database;

import com.example.mechanicaldesignmanager.Task;
import com.example.mechanicaldesignmanager.TaskWorkLog;
import com.example.mechanicaldesignmanager.User;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface TaskRepository extends CrudRepository<Task,Long> {


}
