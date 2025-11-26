package com.example.mechanicaldesignmanager.database;

import com.example.mechanicaldesignmanager.Task;
import org.springframework.data.repository.CrudRepository;


public interface TaskRepository extends CrudRepository<Task,Long> {

}
