package com.example.demo.service;

import com.example.demo.exception.TaskNotFound;
import com.example.demo.exception.UserIdNotFound;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TaskServiceTest {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TaskService taskService;

    private static final int NUMBER_OF_TASK = 5;

    private ArrayList<String> listOfTaskId = new ArrayList<>();
    private ObjectId userId;
    @Before
    public void init(){
        mongoTemplate.remove(User.class).all();
        mongoTemplate.remove(Task.class).all();
        User user = User.builder()
                .name("tranhoi")
                .email("tranhoi199@gmail.com")
                .build();
        mongoTemplate.save(user);
        userId = user.getId();
        for(int i = 0; i < NUMBER_OF_TASK; i++){
            Task task = Task.builder()
                    .name(RandomStringUtils.random(10))
                    .description(RandomStringUtils.random(20))
                    .user(user)
                    .build();
            mongoTemplate.save(task);
            listOfTaskId.add(task.getId().toString());
        }
    }

    @Test
    public void findAllTaskOfUser(){
        List<Task> taskList = taskService.getAllTasksOfUser(userId);
        Assert.assertEquals(NUMBER_OF_TASK, taskList.size());
    }

    @Test
    public void testCreateTaskForUser(){
        Task task = taskService.createTask(RandomStringUtils.random(10), RandomStringUtils.random(10), userId);
        Assert.assertEquals(NUMBER_OF_TASK + 1, taskService.getAllTasksOfUser(userId).size());
    }

    @Test(expected = UserIdNotFound.class)
    public void testWithInvalidUserIdThrowException(){
        Task task = taskService.createTask(RandomStringUtils.random(10), RandomStringUtils.random(10), ObjectId.get());
    }

    @Test
    public void testFindWithValidTaskIdReturnUser(){
        Task task = taskService.findTaskById(new ObjectId(listOfTaskId.get(0)));
        Assert.assertEquals(task.getId().toString(), listOfTaskId.get(0));
    }

    @Test(expected = TaskNotFound.class)
    public void testFindWithInvalidTaskIdThrowException(){
        taskService.findTaskById(ObjectId.get());
    }

    @Test(expected = UserIdNotFound.class)
    public void testUpdateTaskWithInvalidUserIdThrowException(){
        String taskId = listOfTaskId.get(0);
        taskService.updatedTask(ObjectId.get().toString(), taskId, RandomStringUtils.random(10), RandomStringUtils.random(10));
    }

    @Test(expected = TaskNotFound.class)
    public void testUpdateTaskWithValidUserIdButInvalidTaskIdThrowE(){
        taskService.updatedTask(userId.toString(), ObjectId.get().toString(), RandomStringUtils.random(10), RandomStringUtils.random(10));
    }

    @Test
    public void testUpdateWithValidIdsReturnTask(){
        String name = RandomStringUtils.random(20);
        Task task = taskService.updatedTask(userId.toString(), listOfTaskId.get(0), name, RandomStringUtils.random(10));
        Assert.assertEquals(name, task.getName());
    }

    @Test(expected = UserIdNotFound.class)
    public void testDeleteTaskWithInvalidUserIdThrowException(){
        ObjectId taskId = new ObjectId(listOfTaskId.get(0));
        taskService.deleteTaskByIdAndUserId(taskId, ObjectId.get());
    }

    @Test(expected = TaskNotFound.class)
    public void testDeleteTaskWithValidUserIdButInvalidTaskIdThrowE(){
        taskService.deleteTaskByIdAndUserId(userId, ObjectId.get());
    }

    @Test
    public void testDeleteWithValidIdsReturnTask(){
        User user = User.builder()
                .name("12")
                .email("3123")
                .build();
        mongoTemplate.save(user);
        Task task = Task.builder()
                .name(RandomStringUtils.random(10))
                .description(RandomStringUtils.random(20))
                .user(user)
                .build();
        mongoTemplate.save(task);
        System.out.println("user id in task:"+ task.getUser().getId().toString());
        taskService.deleteTaskByIdAndUserId(task.getId(), user.getId());
        Assert.assertEquals(NUMBER_OF_TASK, taskService.getAllTasksOfUser(userId).size());
    }

}
