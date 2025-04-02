package com.example.demo;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.login.LoginRequest;
import com.example.demo.dto.question.PostQuestionRequest;
import com.example.demo.dto.question.PublishQuestionRequest;
import com.example.demo.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;



import com.example.demo.dto.UserRequest;
import com.example.demo.dto.login.LoginRequest;
import com.example.demo.dto.question.PostQuestionRequest;
import com.example.demo.dto.question.PublishQuestionRequest;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepo;
import com.fasterxml.jackson.databind.JsonNode;
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
public class testGame {

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
    public void testCreateAndLoginUser() throws Exception {

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
    }

    @Test
    public void testCreateQuestion() throws Exception {
        PostQuestionRequest questionRequest = new PostQuestionRequest();
        questionRequest.setBody("What numbers bigger then 5?");
        questionRequest.setCorrectAnswers(List.of("8", "9"));

        MvcResult createResult = mockMvc.perform(post("/sa/quiz/questions")
                        .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("user:qwerty".getBytes()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String questionId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asText();

        PublishQuestionRequest publishRequest = new PublishQuestionRequest();
        publishRequest.setPublished(true);

        mockMvc.perform(put("/sa/quiz/questions/" + questionId + "/publish")
                        .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("user:qwerty".getBytes()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publishRequest)))
                .andExpect(status().isNoContent());
    }

}




