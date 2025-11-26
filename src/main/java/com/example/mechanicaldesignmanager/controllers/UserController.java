package com.example.mechanicaldesignmanager.controllers;

import com.example.mechanicaldesignmanager.AddNewUserForm;
import com.example.mechanicaldesignmanager.User;
import com.example.mechanicaldesignmanager.database.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
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

public class UserController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

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
}
