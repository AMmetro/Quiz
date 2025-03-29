package com.example.demo.entity;

import com.example.demo.constant.PlayerStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "player")
/*Класс JsonBinaryType сериализует Java-объекты (например, List<String>)
*в валидный JSON и сохраняет их как JSONB в базе данных.
*/
@TypeDefs({
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class PlayerEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "game_id")
    private String gameId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PlayerStatus status;

    @Type(type = "jsonb")
    @Column(name = "answers", columnDefinition = "jsonb")
    private List<String> answers;

    @Column(name = "created_at", nullable = false, updatable = false)
    private String createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'")
    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    public PlayerEntity() {
    }

    public PlayerEntity(String userId, List<String> answers, PlayerStatus status) {
        this.userId = userId;
        this.answers = answers;
        this.status = status;
        LocalDateTime createdAt = LocalDateTime.now(ZoneOffset.UTC);
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime createdAt = LocalDateTime.now(ZoneOffset.UTC);
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}