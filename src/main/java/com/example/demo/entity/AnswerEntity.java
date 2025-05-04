package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Entity
@Data // Генерирует геттеры, сеттеры, equals(), hashCode(), toString()
@NoArgsConstructor // Генерирует конструктор без аргументов
@AllArgsConstructor // Генерирует конструктор с аргументами для всех полей
@Table(name = "answers")
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})


public class AnswerEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "questionId")
    private String questionId;

    @Column(name = "playerId")
    private String playerId;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

//    public AnswerEntity() {
//    }

    public AnswerEntity(String text, String questionId, String playerId, Boolean status) {
        this.setText(text);
        this.setQuestionId(questionId);
        this.setPlayerId(playerId);
        this.setStatus(status);
        this.createdAt = LocalDateTime.now();
    }

}

