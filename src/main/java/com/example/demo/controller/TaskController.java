package com.example.demo.controller;

import com.example.demo.api.TasksApi;
import com.example.demo.api.model.*;
import com.example.demo.model.Task;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class TaskController implements TasksApi {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ResponseEntity<ObjectCreationSuccessResponse> createTaskOfUser(String userId, @Valid CreateTaskRequest createTaskRequest) {
        Task task = modelMapper.map(createTaskRequest, Task.class);
        Task createTask = taskService.createTask(task.getName(), task.getDescription(), new ObjectId(userId));
        ObjectCreationSuccessResponse result = new ObjectCreationSuccessResponse();
        result.setId(createTask.getId().toString());
        result.setResponseCode(HttpStatus.OK.value());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ObjectDeletionSuccessResponse> deleteTaskOfUser(String userId, String taskId) {
        taskService.deleteTaskByIdAndUserId(new ObjectId(taskId), new ObjectId(userId));
        ObjectDeletionSuccessResponse response = new ObjectDeletionSuccessResponse();
        response.setResponseCode(200);
        response.setId(taskId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private ResponseEntity<TaskListResponse> buildTaskListResponse(List<Task> taskList){
        TaskListResponse taskListResponse = new TaskListResponse();

        if(taskList != null){
            taskList.forEach(item -> taskListResponse.addTasksItem(modelMapper.map(item, TaskResposneModel.class)));;
        }
        return new ResponseEntity<>(taskListResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TaskListResponse> getTasksOfUser(String userId) {
        List<Task> taskList = taskService.getAllTasksOfUser(new ObjectId(userId));
        return buildTaskListResponse(taskList);
    }

    @Override
    public ResponseEntity<TaskResposneModel> updateTaskOfUser(String userId, @Valid UpdateTaskRequest updateTaskRequest) {
        Task task = taskService.updatedTask(userId, updateTaskRequest.getTaskId(), updateTaskRequest.getName(), updateTaskRequest.getDescription());
        TaskResposneModel result = modelMapper.map(task, TaskResposneModel.class);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
