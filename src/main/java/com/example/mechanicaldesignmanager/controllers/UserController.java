package com.example.mechanicaldesignmanager.controllers;

import com.example.mechanicaldesignmanager.AddNewUserForm;
import com.example.mechanicaldesignmanager.Project;
import com.example.mechanicaldesignmanager.Task;
import com.example.mechanicaldesignmanager.User;
import com.example.mechanicaldesignmanager.database.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller


public class UserController {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;

    }

    @GetMapping("/my-profile")
    public String myPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "my-profile";
    }

    @GetMapping("/user/{id}")
    public String userPage (@PathVariable("id") Long id, Model model) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("User not found: " + id));
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user/new")
    public String AddNewUserForm (){
        return "new_user";
    }

    @PostMapping("/user/new")
    public String processAddNewUser (AddNewUserForm form) {
        User savedUser = userRepo.save (form.toUser (passwordEncoder));
        return "redirect:/user/" + savedUser.getId();
    }

    @GetMapping("/project/{projectId}/add_user/list")
    public String usersToAddList (@PathVariable Long projectId, Model model) {
        List<User> users = (ArrayList<User>) userRepo.findAll();
        model.addAttribute("users", users);
        model.addAttribute("projectId", projectId);
        return "add_user_list";
    }

    @GetMapping("/admin_tools/all_users")
    public String getAllUsers(Model model) {
        List<User> allUsers = (ArrayList<User>) userRepo.findAll();
        model.addAttribute("allUsers", allUsers);
        return "all_users";
    }

    @GetMapping("/admin_tools/all_users/edit_user/{id}")
    public String editUser(@PathVariable("id") Long userID, Model model) {
        User user = userRepo.findById(userID).orElseThrow(() ->
                new IllegalArgumentException("User not found: " + userID));
        model.addAttribute("user", user);
        return "edit_user";
    }

    @PostMapping("/admin_tools/all_users/edit_user/{id}")
    public String processEditUser (@PathVariable("id") Long userId, AddNewUserForm form) {
        User user = userRepo.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found: " + userId));

        user.setUsername(form.getUsername());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setEmail(form.getEmail());
        user.setBirthDate(form.getBirthDate());
        user.setEmploymentDate(form.getEmploymentDate());


        if (form.getPassword() != null && !form.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        userRepo.save(user);

        return "redirect:/admin_tools/all_users";
    }

    @PostMapping("/admin_tools/all_users/delete_user/{id}")
    @Transactional
    public String deleteUser(@PathVariable ("id") Long userId){

        User user = userRepo.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found: " + userId));

        for (Project project : user.getProjects()){
            project.getUsers().remove(user);
        }

        for (Task task : user.getTasks()) {
            task.setUser(null);
        }


        userRepo.delete(user);

        return "redirect:/admin_tools/all_users";
    }
}
