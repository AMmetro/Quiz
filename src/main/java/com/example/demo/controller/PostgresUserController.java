package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.service.PostgresUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/postgres/users")
public class PostgresUserController {

    private final PostgresUserService postgresUserService;

    @Autowired
    public PostgresUserController(PostgresUserService postgresUserService) {
        this.postgresUserService = postgresUserService;
    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(postgresUserService.getAllUsers());
    }
} 