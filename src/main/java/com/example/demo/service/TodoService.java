package com.example.demo.service;


import com.example.demo.entity.TodoEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.model.Todo;
import com.example.demo.repository.TodoRepo;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepo todoRepo;
    @Autowired
    private UserRepo userRepo;
//    private UserService userService;

    public Todo createTodo(Long userId, TodoEntity todo) {
        UserEntity user = userRepo.findById(userId).get();
        todo.setUser(user);
        /* после todo.setUser(user)
         * в newTodo храняться данные user{...}
         */
        TodoEntity newTodo = todoRepo.save(todo);
        return Todo.toModelMapper(newTodo);
    }

    public TodoEntity completeTodo(Long id) {
        TodoEntity todo = todoRepo.findById(id).get();
        todo.setCompleted(!todo.getCompleted());
        return todoRepo.save(todo);
    }


}
