package com.example.demo.model;

import com.example.demo.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Transient;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private Long id;
    private String username;
    private LocalDate dob;
    private List<Todo> todos;
    private Long age;

    public User() {}

    public static User toModelMapper (UserEntity entity) {
        User model = new User();

        model.setId(entity.getId());
        model.setUserName(entity.getUsername());
        model.setAge(entity.getCurrentAge(entity.getDob()));
        model.setTodos(entity.getTodos().stream().map(Todo::toModelMapper).collect(Collectors.toList())) ;

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



    public void setUserName(String userName) {
        this.username = userName ;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

//    @Override
//    public String toString() {
//        return "User{id=" + id + ", name='" + username + "'}";
//    }

}
