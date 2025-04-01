package com.example.demo.integration;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.login.LoginRequest;
import com.example.demo.dto.question.PostQuestionRequest;
import com.example.demo.dto.question.PublishQuestionRequest;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    public void testLoginUser() throws Exception {
        // Сначала создаем пользователя
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("user1@test.com");
        userRequest.setPassword("password123");
        userRequest.setLogin("user1");

        mockMvc.perform(post("/sa/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());

        // Подготовка данных для логина
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginOrEmail("user1@test.com");
        loginRequest.setPassword("password123");

        // Выполнение запроса на логин
        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString()) // Проверяем, что ответ - строка (токен)
                .andReturn();

        String accessToken = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("accessToken").asText();

        // Проверяем, что токен не пустой
        String token = result.getResponse().getContentAsString();
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void testCreateQuestion() throws Exception {
        // Сначала создаем пользователя и получаем токен
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("user1@test.com");
        userRequest.setPassword("password123");
        userRequest.setLogin("user1");

        mockMvc.perform(post("/sa/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginOrEmail("user1@test.com");
        loginRequest.setPassword("password123");

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("accessToken").asText();

        // Подготовка данных для создания вопроса
        PostQuestionRequest questionRequest = new PostQuestionRequest();
        questionRequest.setBody("What is the capital of France?");
        questionRequest.setCorrectAnswers(List.of("Paris"));

        // Выполнение запроса на создание вопроса
        MvcResult result = mockMvc.perform(post("/sa/quiz/questions")
                .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("user:qwerty".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.body").value("What is the capital of France?"))
                .andExpect(jsonPath("$.correctAnswers").isArray())
                .andExpect(jsonPath("$.correctAnswers[0]").value("Paris"))
                .andExpect(jsonPath("$.published").value(false))
                .andExpect(jsonPath("$.createdAt").exists())
                .andReturn();
    }

    @Test
    public void testPublishQuestion() throws Exception {
        // Создаем вопрос
        PostQuestionRequest questionRequest = new PostQuestionRequest();
        questionRequest.setBody("What is the capital of France?");
        questionRequest.setCorrectAnswers(List.of("Paris"));

        MvcResult createResult = mockMvc.perform(post("/sa/quiz/questions")
                .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("user:qwerty".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // Получаем id созданного вопроса
        String questionId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asText();

        // Тест 1: Публикуем вопрос (published = true)
        PublishQuestionRequest publishRequest = new PublishQuestionRequest();
        publishRequest.setPublished(true);

        mockMvc.perform(put("/sa/quiz/questions/" + questionId + "/publish")
                .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("user:qwerty".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(publishRequest)))
                .andExpect(status().isNoContent());

        // Тест 2: Снимаем с публикации (published = false)
        publishRequest.setPublished(false);

        mockMvc.perform(put("/sa/quiz/questions/" + questionId + "/publish")
                .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("user:qwerty".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(publishRequest)))
                .andExpect(status().isNoContent());

        // Тест 3: Отправляем некорректное значение в теле запроса
        mockMvc.perform(put("/sa/quiz/questions/" + questionId + "/publish")
                .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("user:qwerty".getBytes()))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"published\": \"invalid\"}"))
                .andExpect(status().isBadRequest());
    }
} 