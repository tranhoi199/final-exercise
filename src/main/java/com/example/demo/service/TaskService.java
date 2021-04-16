package com.example.demo.service;

import com.example.demo.model.Task;
import org.bson.types.ObjectId;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasksOfUser(ObjectId userId);
    Task createTask(String name, String description, ObjectId userId);
    Task updatedTask(String userId, String taskId, String name, String description);
    Task findTaskById(ObjectId id);
    void deleteTaskByIdAndUserId(ObjectId id, ObjectId userId);
}
