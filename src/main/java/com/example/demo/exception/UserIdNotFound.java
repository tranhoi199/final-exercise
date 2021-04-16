package com.example.demo.exception;

public class UserIdNotFound extends RuntimeException{
    public UserIdNotFound(String id){
        super(String.format("User with id %s not found", id));
    }
}
