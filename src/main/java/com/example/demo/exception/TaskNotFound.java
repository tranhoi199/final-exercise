package com.example.demo.exception;

public class TaskNotFound extends RuntimeException{
    public TaskNotFound(String id){
        super(String.format("Task with id- %s not found", id));
    }
}
