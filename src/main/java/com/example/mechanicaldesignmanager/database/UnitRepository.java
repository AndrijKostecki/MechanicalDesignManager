package com.example.mechanicaldesignmanager.database;

import com.example.mechanicaldesignmanager.Project;
import com.example.mechanicaldesignmanager.Unit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UnitRepository extends CrudRepository<Unit,Long> {

    List<Unit> findByProject(Project project);


}

