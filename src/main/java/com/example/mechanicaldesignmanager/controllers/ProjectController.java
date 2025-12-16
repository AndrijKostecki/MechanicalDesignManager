package com.example.mechanicaldesignmanager.controllers;

import com.example.mechanicaldesignmanager.*;
import com.example.mechanicaldesignmanager.database.ProjectRepository;
import com.example.mechanicaldesignmanager.database.UserRepository;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class ProjectController {

    private ProjectRepository projectRep;
    private UserRepository userRep;

    public ProjectController(ProjectRepository projectRep,  UserRepository userRep) {

        this.projectRep = projectRep;
        this.userRep = userRep;
    }



    @GetMapping("/project/new")
    public String AddNewProjectForm (){
        return "new_project";
    }

    @PostMapping("/project/new")
    public String processAddNewProject (AddNewProjectForm form) {
        Project savedProject = projectRep.save (form.toProject ());
        return "redirect:/project/"+savedProject.getId();
    }

    @GetMapping("/project/{id}")
    public String projectPage (@PathVariable("id") Long id, Model model) {
        Project project = projectRep.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Project not found: " + id));
        List<Unit> units = project.getUnits();
        List<User> users = project.getUsers();
        model.addAttribute("project", project);
        model.addAttribute("units", units);
        model.addAttribute("users", users);

        return "project";
    }

    @GetMapping("/admin_tools/projects_list")
    public String GetProjectsList (Model model){
        ArrayList<Project> projectList = (ArrayList<Project>) projectRep.findAll();
        model.addAttribute("projects", projectList);
        return "projects_list";
    }

    @PostMapping ("/project/{projectId}/add_user/{userId}")
    public String addUserToProject (@PathVariable Long projectId, @PathVariable Long userId){
        Project project = projectRep.findById(projectId).orElseThrow(() ->
                new IllegalArgumentException("Project not found: " + projectId));
        User user = userRep.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found: " + userId));
        project.getUsers().add(user);
        projectRep.save(project);

        user.getProjects().add(project);
        userRep.save(user);

        return "redirect:/project/" + projectId;

    }

    @PostMapping("/project/{projectId}/remove_user_from_project/{userId}")
    public String removeUserFromProject (@PathVariable Long projectId, @PathVariable Long userId){

        Project project = projectRep.findById(projectId).orElseThrow(() ->
                new IllegalArgumentException("Project not found: " + projectId));
        User user = userRep.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found: " + userId));
        project.getUsers().remove(user);
        projectRep.save(project);

        user.getProjects().remove(project);
        userRep.save(user);


        return "redirect:/project/" + projectId;
    }

    @GetMapping("/admin_tools/projects_list/edit_project/{id}")
    public String editProject (@PathVariable("id") Long projectId, Model model){

        Project project = projectRep.findById(projectId).orElseThrow(()->
                new IllegalArgumentException("Project not found "+ projectId));

        model.addAttribute("project", project);


        return "edit_project";
    }

    @PostMapping("/admin_tools/projects_list/edit_project/{id}")
    public String processEditProject (@PathVariable("id") Long projectId, AddNewProjectForm form){

        Project project = projectRep.findById(projectId).orElseThrow(()->
        new IllegalArgumentException("Project not found "+ projectId));


        project.setTitle(form.getTitle());
        project.setDescription(form.getDescription());
        project.setStartDate(form.getStartDate());
        project.setEndDate(form.getEndDate());

        projectRep.save(project);

        return "redirect:/admin_tools/projects_list";
    }

    @PostMapping("/admin_tools/projects_list/delete_project/{id}")
    @Transactional
    public String deleteProject(@PathVariable Long id) {

        Project project = projectRep.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Project not found " + id));


        for (User user : new ArrayList<>(project.getUsers())) {
            user.getProjects().remove(project);
        }
        project.getUsers().clear();


        projectRep.delete(project);

        return "redirect:/admin_tools/projects_list";
    }


}
