package com.example.demo.integration;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.login.LoginRequest;
import com.example.demo.dto.question.PostQuestionRequest;
import com.example.demo.dto.question.PublishQuestionRequest;
import com.example.demo.repository.GameRepository;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.QuestionRepo;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccessRigthForGameFlow25_3 {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private QuestionRepo questionRepo;

    @BeforeEach
    public void setUp() {
        userRepo.deleteAll();
        gameRepository.deleteAll();
        playerRepository.deleteAll();
        questionRepo.deleteAll();
    }

    @Test
    public void testCreateGameWith2User() throws Exception {

//        Create user1:
        UserRequest userRequest1 = new UserRequest();
        userRequest1.setEmail("user1@test.com");
        userRequest1.setPassword("password123");
        userRequest1.setLogin("user1");

        mockMvc.perform(post("/sa/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest1)))
                .andExpect(status().isCreated());

//        Login user1
        LoginRequest loginRequest1 = new LoginRequest();
        loginRequest1.setLoginOrEmail("user1@test.com");
        loginRequest1.setPassword("password123");

        MvcResult loginResult1 = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest1)))
                .andExpect(status().isOk())
                .andReturn();

        String accessToken1 = objectMapper.readTree(loginResult1.getResponse().getContentAsString())
                .get("accessToken").asText();


   // CREATE QUESTIONS
    for (int i = 1; i <= 6; i++) {
        // Create one question
        PostQuestionRequest questionRequest = new PostQuestionRequest();
        questionRequest.setBody("Question " + i + ": What numbers are bigger than " + (5 + i) + "?");
        questionRequest.setCorrectAnswers(List.of(String.valueOf(8 + i), String.valueOf(9 + i)));

        MvcResult createResult = mockMvc.perform(post("/sa/quiz/questions")
                        .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("user:qwerty".getBytes()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // Get id of question
        String questionId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asText();

        // Publish question
        PublishQuestionRequest publishRequest = new PublishQuestionRequest();
        publishRequest.setPublished(true);

        mockMvc.perform(put("/sa/quiz/questions/" + questionId + "/publish")
                        .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("user:qwerty".getBytes()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publishRequest)))
                .andExpect(status().isNoContent());
    }


//        Create game by User1 in pending status
        MvcResult game1 = mockMvc.perform(post("/pair-game-quiz/pairs/connection")
                        .header("Authorization", "Bearer " + accessToken1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstPlayerProgress").doesNotExist())
                .andExpect(jsonPath("$.firstPlayerProgress.answers").doesNotExist())
                .andExpect(jsonPath("$.firstPlayerProgress.player.id").doesNotExist())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.pairCreatedDate").exists())
                .andReturn();

//        Create user2:
        UserRequest userRequest2 = new UserRequest();
        userRequest2.setEmail("user2@test.com");
        userRequest2.setPassword("password123");
        userRequest2.setLogin("user2");

        mockMvc.perform(post("/sa/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest2)))
                .andExpect(status().isCreated());

//        Login user2
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

//        Connect to game by User2 and switсh game to active status
        MvcResult connectionUser2 = mockMvc.perform(post("/pair-game-quiz/pairs/connection")
                        .header("Authorization", "Bearer " + accessToken2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstPlayerProgress").doesNotExist())
                .andExpect(jsonPath("$.firstPlayerProgress.answers").doesNotExist())
                .andExpect(jsonPath("$.firstPlayerProgress.player.id").doesNotExist())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.pairCreatedDate").exists())
                .andReturn();

//       Create user3:
        UserRequest userRequest3 = new UserRequest();
        userRequest3.setEmail("user3@test.com");
        userRequest3.setPassword("password123");
        userRequest3.setLogin("user3");

        mockMvc.perform(post("/sa/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest3)))
                .andExpect(status().isCreated());

//       Login user3
        LoginRequest loginRequest3 = new LoginRequest();
        loginRequest3.setLoginOrEmail("user3@test.com");
        loginRequest3.setPassword("password123");

        MvcResult loginResult3 = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest3)))
                .andExpect(status().isOk())
                .andReturn();

        String accessToken3 = objectMapper.readTree(loginResult3.getResponse().getContentAsString())
                .get("accessToken").asText();














//         Create user 2
//        UserRequest userRequest2 = new UserRequest();
//        userRequest2.setEmail("user2@test.com");
//        userRequest2.setPassword("password123");
//        userRequest2.setLogin("user2");
//
//        mockMvc.perform(post("/sa/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userRequest2)))
//                .andExpect(status().isCreated());
//
//        LoginRequest loginRequest2 = new LoginRequest();
//        loginRequest2.setLoginOrEmail("user2@test.com");
//        loginRequest2.setPassword("password123");
//
//        MvcResult loginResult2 = mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest2)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String accessToken2 = objectMapper.readTree(loginResult2.getResponse().getContentAsString())
//                .get("accessToken").asText();


//        Create game for user2 in active status
//        MvcResult gameActiveUser2 = mockMvc.perform(post("/pair-game-quiz/pairs/connection")
//                        .header("Authorization", "Bearer " + accessToken2))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").exists())
////                .andExpect(jsonPath("$.firstPlayerProgress").exists())
////                .andExpect(jsonPath("$.firstPlayerProgress.answers").isArray())
////                .andExpect(jsonPath("$.firstPlayerProgress.player.id").exists())
////                .andExpect(jsonPath("$.firstPlayerProgress.player.login").value("user2"))
////                .andExpect(jsonPath("$.firstPlayerProgress.score").value(0))
////                .andExpect(jsonPath("$.secondPlayerProgress").exists())
////                .andExpect(jsonPath("$.secondPlayerProgress.answers").isArray())
////                .andExpect(jsonPath("$.secondPlayerProgress.player").value(""))
////                .andExpect(jsonPath("$.secondPlayerProgress.player.login").value("login2"))
////                .andExpect(jsonPath("$.secondPlayerProgress.score").value(0))
////                .andExpect(jsonPath("$.questions").isArray())
//                .andExpect(jsonPath("$.status").value("ACTIVE"))
//                .andExpect(jsonPath("$.pairCreatedDate").exists())
//                .andExpect(jsonPath("$.startGameDate").exists())
//                .andExpect(jsonPath("$.finishGameDate").doesNotExist())
//                .andReturn();


////        Get id of game2
//        String gameId = objectMapper.readTree(gameActiveUser2.getResponse().getContentAsString())
//                .get("id").asText();

////          Try to get game by user with id that not in game
//        mockMvc.perform(get("/pair-game-quiz/pairs/" + gameId)
//                        .header("Authorization", "Bearer " + accessToken2))
//                .andExpect(status().isForbidden());

    }

}