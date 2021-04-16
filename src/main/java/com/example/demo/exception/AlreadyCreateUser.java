package com.example.demo.exception;

public class AlreadyCreateUser extends RuntimeException{
    public AlreadyCreateUser(String email){
        super(String.format("User with email %s already exist", email));
    }
}
