package com.example.mechanicaldesignmanager;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "units")
@Data
public class Unit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;


    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;



    @OneToMany(mappedBy = "unit")
    private List<Task> tasks = new ArrayList<>();


    public Unit(String title, String description, Project project) {
        this.title = title;
        this.description = description;
        this.project = project;
    }
}
