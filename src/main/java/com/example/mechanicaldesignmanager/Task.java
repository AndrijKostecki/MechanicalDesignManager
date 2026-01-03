package com.example.mechanicaldesignmanager;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "tasks")

public class Task implements Serializable {

    public enum taskStatus {
        OPENED,
        CHECKING,
        COMPLETED

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_name")
    private String taskName;
    private String description;


    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany (mappedBy = "task")
    private List<TaskWorkLog> taskWorkLogs = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private taskStatus status;



}
