package com.example.demo.service;

import com.example.demo.exception.TaskNotFound;
import com.example.demo.exception.UserIdNotFound;
import com.example.demo.model.QTask;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImp implements TaskService{
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;
    @Override
    public List<Task> getAllTasksOfUser(ObjectId userId) {
        userService.findUserById(userId);
        QTask qTask = QTask.task;
        Iterable<Task> taskList = taskRepository.findAll(qTask.user.id.eq(userId));
        List<Task> taskList1 = new ArrayList<>();
        taskList.forEach(taskList1::add);
        return taskList1;
    }

    @Override
    public Task createTask(String name, String description, ObjectId userId) {
        User user = userService.findUserById((userId));
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setUser(user);
        taskRepository.save(task);
        return task;
    }

    @Override
    public Task updatedTask(String userId, String taskId, String name, String description) {
        //check if valid user id
        userService.findUserById(new ObjectId(userId));
        //find task by id
        Task task = findTaskById(new ObjectId(taskId));
        if(name != "" && name != null){
            task.setName(name);
        }
        if(description != "" && description != null){
            task.setDescription(description);
        }
        taskRepository.save(task);
        return task;
    }

    @Override
    public Task findTaskById(ObjectId id) {
        Task task = taskRepository.findTaskById(id);
        if(task == null){
            throw new TaskNotFound(id.toString());
        }
        return task;
    }

    @Override
    public void deleteTaskByIdAndUserId(ObjectId id, ObjectId userId) {
        Task task = findTaskById(id);

        QTask qTask = QTask.task;
        Iterable<Task> tasks = taskRepository.findAll(qTask.user.id.eq(userId));
        if(tasks.iterator().hasNext() == false){
            throw new UserIdNotFound(userId.toString());
        }
        taskRepository.delete(task);
    }
}
