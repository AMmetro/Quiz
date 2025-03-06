package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.PostgresUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostgresUserService {

    private final PostgresUserRepository postgresUserRepository;

    @Autowired
    public PostgresUserService(PostgresUserRepository postgresUserRepository) {
        this.postgresUserRepository = postgresUserRepository;
    }

    public List<UserEntity> getAllUsers() {
        return postgresUserRepository.findAllUsers();
    }
} 