package com.example.demo.integration;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.login.LoginRequest;
import com.example.demo.dto.question.PostQuestionRequest;
import com.example.demo.dto.question.PublishQuestionRequest;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.*;
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
public class AccessRigthForGameFlow25_2 {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private AnswerRepo answerRepo;

    @BeforeEach
    public void setUp() {
        userRepo.deleteAll();
        gameRepository.deleteAll();
        playerRepository.deleteAll();
        questionRepo.deleteAll();
        answerRepo.deleteAll();
    }

    @Test
    public void testCreateGameWith2User() throws Exception {

//        Create user1:
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

        MvcResult loginResult1 = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = objectMapper.readTree(loginResult1.getResponse().getContentAsString())
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

//        Create game by User1 in pending status
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

//         Create user 2
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


//        Join user2 to game, switch game to active status
        MvcResult gameActiveUser2 = mockMvc.perform(post("/pair-game-quiz/pairs/connection")
                        .header("Authorization", "Bearer " + accessToken2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.pairCreatedDate").exists())
                .andExpect(jsonPath("$.startGameDate").exists())
                .andExpect(jsonPath("$.finishGameDate").doesNotExist())
                .andReturn();

//        Try to get existing active user game by user1
        mockMvc.perform(get("/pair-game-quiz/pairs/my-current/")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstPlayerProgress").exists())
                .andExpect(jsonPath("$.firstPlayerProgress.answers").isArray())
                .andExpect(jsonPath("$.firstPlayerProgress.player.id").exists())
                .andExpect(jsonPath("$.firstPlayerProgress.player.login").value("user1"))
                .andExpect(jsonPath("$.firstPlayerProgress.score").value(0))
                .andExpect(jsonPath("$.secondPlayerProgress").exists())
                .andExpect(jsonPath("$.secondPlayerProgress.answers").isArray())
                .andExpect(jsonPath("$.secondPlayerProgress.player.id").exists())
                .andExpect(jsonPath("$.secondPlayerProgress.player.login").value("user2"))
                .andExpect(jsonPath("$.secondPlayerProgress.score").value(0))
                .andExpect(jsonPath("$.questions").isArray())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.pairCreatedDate").exists())
                .andExpect(jsonPath("$.startGameDate").exists())
                .andExpect(jsonPath("$.finishGameDate").doesNotExist())
                .andReturn();

//        Try to get existing active user game by user2
mockMvc.perform(get("/pair-game-quiz/pairs/my-current/")
                .header("Authorization", "Bearer " + accessToken2)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.firstPlayerProgress").exists())
        .andExpect(jsonPath("$.firstPlayerProgress.answers").isArray())
        .andExpect(jsonPath("$.firstPlayerProgress.player.id").exists())
        .andExpect(jsonPath("$.firstPlayerProgress.player.login").value("user1"))
        .andExpect(jsonPath("$.firstPlayerProgress.score").value(0))
        .andExpect(jsonPath("$.secondPlayerProgress").exists())
        .andExpect(jsonPath("$.secondPlayerProgress.answers").isArray())
        .andExpect(jsonPath("$.secondPlayerProgress.player.id").exists())
        .andExpect(jsonPath("$.secondPlayerProgress.player.login").value("user2"))
        .andExpect(jsonPath("$.secondPlayerProgress.score").value(0))
        .andExpect(jsonPath("$.questions").isArray())
        .andExpect(jsonPath("$.status").value("ACTIVE"))
        .andExpect(jsonPath("$.pairCreatedDate").exists())
        .andExpect(jsonPath("$.startGameDate").exists())
        .andExpect(jsonPath("$.finishGameDate").doesNotExist())
        .andReturn();

//        Get id of game2
        String gameId = objectMapper.readTree(gameActiveUser2.getResponse().getContentAsString())
                .get("id").asText();

//        Try to get game by user with id that not in game
        mockMvc.perform(get("/pair-game-quiz/pairs/" + gameId)
                        .header("Authorization", "Bearer " + accessToken2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get("/pair-game-quiz/pairs/" + gameId)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

    }

}