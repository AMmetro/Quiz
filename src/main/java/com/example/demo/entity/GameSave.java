//package com.example.demo.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.Type;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//import java.util.List;
//
//public class GameSave {
//
//    @Entity
//    @Data // Генерирует геттеры, сеттеры, equals(), hashCode(), toString()
//    @NoArgsConstructor // Генерирует конструктор без аргументов
//    @AllArgsConstructor // Генерирует конструктор с аргументами для всех полей
//    @Table(name = "games")
////@TypeDefs({
////        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
////})
//    public class GameEntity {
//        @Id
//        @GeneratedValue(generator = "UUID")
//        @Column(name = "id", updatable = false, nullable = false)
//        private String id;
//
//        @Column(name = "player_1", unique = true)
//        private String player_1;
//
//        @Column(name = "player_2", unique = true)
//        private String player_2;
//
//        @Column(name = "user_1")
//        private String user_1;
//
//        @Column(name = "user_2")
//        private String user_2;
//
//        //    @Enumerated(EnumType.STRING)
//        @Column(name = "status", nullable = false)
//        private String status;
//
//        @Type(type = "jsonb")
//        @Column(name = "questions", columnDefinition = "jsonb")
//        private List<String> questions;
//
//        @Column(name = "pairCreatedAt")
//        private LocalDateTime pairCreatedAt;
//
//        public GameEntity(String player_1, String user_1, List<String> questions ) {
//            this.player_1 = player_1;
//            this.user_1 = user_1;
//            this.player_2 = null;
//            this.user_2 = null;
////        this.status = GameStatus.PENDING;
//            this.questions = questions;
//            pairCreatedAt = LocalDateTime.now(ZoneOffset.UTC);
//        }
//
//
//    }
