package com.example.mechanicaldesignmanager.database;


import com.example.mechanicaldesignmanager.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User,Long> {

    User findByUsername(String username);

}

