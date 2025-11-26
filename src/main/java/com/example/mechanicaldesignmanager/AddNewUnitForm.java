package com.example.mechanicaldesignmanager;

import lombok.Data;



@Data
public class AddNewUnitForm {


    private String title;
    private String description;
    private Project project;


    public Unit toUnit () {
        return new Unit(
                title, description, project);
    }
}
