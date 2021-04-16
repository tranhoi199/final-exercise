package com.example.demo.service;

import com.example.demo.model.User;
import org.bson.types.ObjectId;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User createUser(User user);
    User findUserByEmail(String email);
    User findUserById(ObjectId id);
    List<User> findUsersByPrefixName(String prefix);
}
