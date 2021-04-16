package com.example.demo.repository;

import com.example.demo.model.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TaskRepository extends MongoRepository<Task, String>, QuerydslPredicateExecutor<Task> {
    Task findTaskById(ObjectId taskId);
}
