package com.example.demo.controller;

import com.example.demo.api.UsersApi;
import com.example.demo.api.model.CreateUserRequest;
import com.example.demo.api.model.ObjectCreationSuccessResponse;
import com.example.demo.api.model.UserListResponse;
import com.example.demo.api.model.UserResponseModel;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class UserController implements UsersApi {
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ResponseEntity<ObjectCreationSuccessResponse> createUser(@Valid CreateUserRequest createUserRequest) {
        User user = modelMapper.map(createUserRequest, User.class);
        User persistUser = userService.createUser(user);
        ObjectCreationSuccessResponse result = new ObjectCreationSuccessResponse();
        result.setId(persistUser.getId().toString());
        result.setResponseCode(HttpStatus.CREATED.value());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserListResponse> findUserByEmail(@Valid String email) {
        System.out.println("email:"+email);
        User user = userService.findUserByEmail(email);
        List<User> userList = new ArrayList<>();
        if(user != null) {
            userList.add(user);
        }
        System.out.println("log user:"+user);
        return buildUserListResponse(userList);
    }

    private ResponseEntity<UserListResponse> buildUserListResponse(List<User> userList) {
        UserListResponse userListResponse = new UserListResponse();
        if(userList != null) {
            userList.forEach(item -> userListResponse.addUsersItem(modelMapper.map(item, UserResponseModel.class)));
        }
        return new ResponseEntity<>(userListResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserListResponse> getUserList() {
        List<User> userList = userService.getAllUsers();
        return buildUserListResponse(userList);
    }
}
