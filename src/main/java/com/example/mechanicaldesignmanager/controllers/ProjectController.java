package com.example.mechanicaldesignmanager.controllers;

import com.example.mechanicaldesignmanager.AddNewProjectForm;
import com.example.mechanicaldesignmanager.Project;
import com.example.mechanicaldesignmanager.Unit;
import com.example.mechanicaldesignmanager.User;
import com.example.mechanicaldesignmanager.database.ProjectRepository;
import com.example.mechanicaldesignmanager.database.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/project")
public class ProjectController {

    private ProjectRepository projectRep;
    private UserRepository userRep;

    public ProjectController(ProjectRepository projectRep,  UserRepository userRep) {

        this.projectRep = projectRep;
        this.userRep = userRep;
    }



    @GetMapping("/new")
    public String AddNewProjectForm (){
        return "new_project";
    }

    @PostMapping("/new")
    public String processAddNewProject (AddNewProjectForm form) {
        Project savedProject = projectRep.save (form.toProject ());
        return "redirect:/project/"+savedProject.getId();
    }

    @GetMapping("/{id}")
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

    @GetMapping("/projects_list")
    public String GetProjectsList (Model model){
        ArrayList<Project> projectList = (ArrayList<Project>) projectRep.findAll();
        model.addAttribute("projects", projectList);
        return "projects_list";
    }

    @PostMapping ("/{projectId}/add_user/{userId}")
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

}
