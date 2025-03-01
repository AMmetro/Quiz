package com.example.demo.repository;

import com.example.demo.entity.TodoEntity;
import com.example.demo.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepo extends CrudRepository<TodoEntity, Long> {
//    TodoEntity findByUsername(String username);
}
