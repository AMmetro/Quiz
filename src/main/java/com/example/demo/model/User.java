package com.example.demo.model;

import com.example.demo.entity.QuestionEntity;
import com.example.demo.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String id;
    private LocalDate dob;
    private List<Todo> todos;
    private Long age;
    private String login;
    private String email;
    private String createdAt;

    public User() {}

    public static User toModelMapper (UserEntity entity) {
        User model = new User();
        LocalDateTime createdAt = LocalDateTime.now(ZoneOffset.UTC);
        model.setId(String.valueOf(entity.getId()));
        model.setEmail(entity.getEmail());
        model.setCreatedAt(entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));
        model.setLogin(entity.getLogin());
        /*
        * model.setAge(entity.getCurrentAge(entity.getDob()));
        * model.setTodos(entity.getTodos().stream().map(Todo::toModelMapper).collect(Collectors.toList())) ;
        */
         return model;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Long getAge() {
        return this.age;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
