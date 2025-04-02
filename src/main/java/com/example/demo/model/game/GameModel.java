package com.example.demo.model.game;

import com.example.demo.constant.GameStatus;
import com.example.demo.entity.QuestionEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameModel {
    private String id;
    private PlayerProgress firstPlayerProgress;
    private PlayerProgress secondPlayerProgress;
    private List<QuestionEntity> questions;
    private GameStatus status;
    private LocalDateTime pairCreatedDate;
    private LocalDateTime startGameDate;
    private LocalDateTime finishGameDate;

//    public GameModel(String id, PlayerProgress firstPlayerProgress,
//                     List<QuestionEntity> questions, GameStatus status, LocalDateTime pairCreatedDate,
//                     LocalDateTime startGameDate, LocalDateTime finishGameDate) {
//        this.id = id;
//        this.firstPlayerProgress = firstPlayerProgress;
////        this.secondPlayerProgress = secondPlayerProgress;
//        this.questions = questions;
//        this.status = status;
//        this.pairCreatedDate = pairCreatedDate;
//        this.startGameDate = startGameDate;
//        this.finishGameDate = finishGameDate;
//    }

    public GameModel(String id, PlayerProgress firstPlayerProgress, PlayerProgress secondPlayerProgress,
                     List<QuestionEntity> questions, GameStatus status, LocalDateTime pairCreatedDate,
                     LocalDateTime startGameDate, LocalDateTime finishGameDate) {
        this.id = id;
        this.firstPlayerProgress = firstPlayerProgress;
        this.secondPlayerProgress = secondPlayerProgress;
        this.questions = questions;
        this.status = status;
        this.pairCreatedDate = pairCreatedDate;
        this.startGameDate = startGameDate;
        this.finishGameDate = finishGameDate;
    }

    // Вложенные классы для структуры данных
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlayerProgress {
        private List<Answer> answers;
        private Player player;
        private int score;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Answer {
        private String questionId;
        private String answerStatus;
        private LocalDateTime addedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Player {
        private String id;
        private String login;
    }

//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class ModelQuestion {
//        private String id;
//        private String body;
//    }

}


