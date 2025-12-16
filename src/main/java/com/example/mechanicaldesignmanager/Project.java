package com.example.mechanicaldesignmanager;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table (name = "projects")
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;
        private String title;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;

        @ManyToMany(mappedBy = "projects")
        private List<User> users = new ArrayList<>();

        @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Unit> units = new ArrayList<>();   


    public Project (String title, String description, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }


}
