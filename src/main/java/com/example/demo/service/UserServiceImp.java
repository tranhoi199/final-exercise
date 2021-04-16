package com.example.demo.service;

import com.example.demo.exception.AlreadyCreateUser;
import com.example.demo.exception.EmailNotFound;
import com.example.demo.exception.UserIdNotFound;
import com.example.demo.model.QUser;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        User findUser = userRepository.findUsersByEmail(user.getEmail());
        if(findUser != null){
            throw new AlreadyCreateUser(user.getEmail());
        }
        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        User users = userRepository.findUsersByEmail(email);
        if(users == null){
            throw new EmailNotFound(email);
        }
        return userRepository.findUsersByEmail(email);
    }

    @Override
    public User findUserById(ObjectId id) {
        User user = userRepository.findUserById(id);
        if(user == null){
            throw new UserIdNotFound(id.toString());
        }
        return user;
    }

    @Override
    public List<User> findUsersByPrefixName(String prefix) {
        QUser userQuery = QUser.user;

        return (List<User>) userRepository.findAll(userQuery.name.startsWith(prefix));
    }
}
