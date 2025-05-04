package com.example.demo.integration;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.login.LoginRequest;
import com.example.demo.dto.question.PostQuestionRequest;
import com.example.demo.dto.question.PublishQuestionRequest;
import com.example.demo.repository.GameRepository;
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

import java.util.List;

        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccessRigthForGameFlow25_1 {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    public void setUp() {
        userRepo.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    public void testCreateGame() throws Exception {

//        Create user:
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("user1@test.com");
        userRequest.setPassword("password123");
        userRequest.setLogin("user1");

        mockMvc.perform(post("/sa/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());
//        Login user
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

//        Create new question
        PostQuestionRequest questionRequest = new PostQuestionRequest();
        questionRequest.setBody("What numbers bigger then 5?");
        questionRequest.setCorrectAnswers(List.of("8", "9"));

        MvcResult createResult = mockMvc.perform(post("/sa/quiz/questions")
                        .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("user:qwerty".getBytes()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionRequest)))
                .andExpect(status().isCreated())
                .andReturn();

//         Get id of question
        String questionId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asText();

//         Publish question
        PublishQuestionRequest publishRequest = new PublishQuestionRequest();
        publishRequest.setPublished(true);

        mockMvc.perform(put("/sa/quiz/questions/" + questionId + "/publish")
                        .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("user:qwerty".getBytes()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publishRequest)))
                .andExpect(status().isNoContent());

//        Create game for user in pending status
        MvcResult result = mockMvc.perform(post("/pair-game-quiz/pairs/connection")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstPlayerProgress").doesNotExist())
                .andExpect(jsonPath("$.firstPlayerProgress.answers").doesNotExist())
                .andExpect(jsonPath("$.firstPlayerProgress.player.id").doesNotExist())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.pairCreatedDate").exists())
                .andReturn();

//        Try to get active user game but it not exist
        mockMvc.perform(get("/pair-game-quiz/pairs/my-current/")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

//        Create user with wrong token
        mockMvc.perform(post("/pair-game-quiz/pairs/connection")
                        .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isUnauthorized());

//         Create game for user if he is already in game
//        TODO FIX ERROR BY PARSING TOKEN IN RESOLVER
        mockMvc.perform(post("/pair-game-quiz/pairs/connection")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isForbidden());

//         Get id of game
        String gameId = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id").asText();

//         Create second user
        UserRequest userRequest2 = new UserRequest();
        userRequest2.setEmail("user2@test.com");
        userRequest2.setPassword("password123");
        userRequest2.setLogin("user2");

        mockMvc.perform(post("/sa/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest2)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest2 = new LoginRequest();
        loginRequest2.setLoginOrEmail("user2@test.com");
        loginRequest2.setPassword("password123");

        MvcResult loginResult2 = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest2)))
                .andExpect(status().isOk())
                .andReturn();

        String accessToken2 = objectMapper.readTree(loginResult2.getResponse().getContentAsString())
                .get("accessToken").asText();

//          Try to get game by user with id that not in game
        mockMvc.perform(get("/pair-game-quiz/pairs/" + gameId)
                        .header("Authorization", "Bearer " + accessToken2))
                .andExpect(status().isForbidden());

    }

}
