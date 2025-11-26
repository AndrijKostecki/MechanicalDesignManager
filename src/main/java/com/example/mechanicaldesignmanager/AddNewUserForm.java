package com.example.mechanicaldesignmanager;


import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;


@Data
public class AddNewUserForm {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private LocalDate employmentDate;

    public User toUser (PasswordEncoder passwordEncoder) {
        return new User(
                username, passwordEncoder.encode(password),
                firstName, lastName, email, birthDate, employmentDate);
    }
}
