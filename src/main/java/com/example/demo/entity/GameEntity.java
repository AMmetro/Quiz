package com.example.demo.entity;

import javax.persistence.*;

import com.example.demo.constant.GameStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Data // Генерирует геттеры, сеттеры, equals(), hashCode(), toString()
@NoArgsConstructor // Генерирует конструктор без аргументов
@AllArgsConstructor // Генерирует конструктор с аргументами для всех полей
@Table(name = "games")
public class GameEntity {
    @Id
    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(
//            name = "UUID",
//            strategy = "org.hibernate.id.UUIDGenerator"
//    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "player_1", unique = true)
    private String player_1;

    @Column(name = "player_2", unique = true)
    private String player_2;

    @Column(name = "user_1")
    private String user_1;

    @Column(name = "user_2")
    private String user_2;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GameStatus status;

    @Column(name = "pairCreatedAt")
    private LocalDateTime pairCreatedAt;
}
