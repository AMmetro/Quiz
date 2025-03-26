package com.example.demo.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "question_table")
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class QuestionEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Type(type = "jsonb")
    @Column(name = "correct_answers", columnDefinition = "jsonb")
    private List<String> correctAnswers;

    @Column(name = "created_at", nullable = false, updatable = false)
    private String createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'")
    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "published", nullable = false)
    private boolean published = false;

    public QuestionEntity() {
    }

    /*  @PrePersist  - аннотация JPA - определяет метод, который должен быть выполнен перед тем,
     *  как сущность будет сохранена в базу данных в первый раз.
     */
    @PrePersist
    protected void onCreate() {
//        createdAt = LocalDateTime.now(ZoneOffset.UTC);
//        updatedAt = null;
        LocalDateTime createdAt = LocalDateTime.now(ZoneOffset.UTC);
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now(ZoneOffset.UTC); // обновляем только при изменении
    }

    public QuestionEntity(String body, List<String> correctAnswers) {
        this.body = body;
        this.correctAnswers = correctAnswers;
        LocalDateTime createdAt = LocalDateTime.now(ZoneOffset.UTC);
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        this.updatedAt = null;
        this.published = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
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

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    @Override
    public String toString() {
        return "QuestionEntity{" +
                "id=" + id +
                ", questionText='" + body + '\'' +
                ", answerOptions=" + correctAnswers +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", published=" + published +
                '}';
    }
}


