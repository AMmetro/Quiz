package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.repository.QuestionRepo;
import com.example.demo.repository.TodoRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("testing")
public class Testing {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TodoRepo todoRepo;

    @Autowired
    private QuestionRepo questionRepo;

    @DeleteMapping(value = "all-data")
    public ResponseEntity deleteAll() {
        try {
            todoRepo.deleteAll();
            userRepo.deleteAll();
            questionRepo.deleteAll();

            return ResponseEntity.noContent().build(); // 204
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при удалении данных: " + e.getMessage());
        }
    }
}
