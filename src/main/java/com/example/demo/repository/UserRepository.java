package com.example.demo.repository;

import com.example.demo.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String>, QuerydslPredicateExecutor<User> {
    User findByEmail(String email);
    User findUsersByEmail(String email);
    User findUserById(ObjectId id);
}
