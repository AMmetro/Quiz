package com.example.demo.integration;

import com.example.demo.dto.UserRequest;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void setUp() {
        userRepo.deleteAll();
    }

    @Test
    public void testCreateUser() throws Exception {
        // Подготовка данных
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("user1@test.com");
        userRequest.setPassword("password123");
        userRequest.setLogin("user1");
//        userRequest.setAge(25L);

        // Выполнение запроса
        MvcResult result = mockMvc.perform(post("/sa/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.login").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@test.com"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andReturn();

        // Проверка сохранения в базе данных
        Optional<UserEntity> savedUser = userRepo.findByEmailOrLogin("user1@test.com");
        assertTrue(savedUser.isPresent());
        assertEquals("user1", savedUser.get().getLogin());
        assertEquals("user1@test.com", savedUser.get().getEmail());
//        assertEquals(25L, savedUser.get().getAge());
    }
} 