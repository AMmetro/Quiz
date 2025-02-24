package com.example.demo.model;

import com.example.demo.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Optional;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private Long id;
    private String username;

    public User() {}

    public static User toModelMapper (UserEntity entity) {
        User model = new User();
        model.setId(entity.getId());
        model.setUserName(entity.getUsername());
        return model;
    }

    public void setUserName(String userName) {
        this.username = userName ;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    @Override
//    public String toString() {
//        return "User{id=" + id + ", name='" + username + "'}";
//    }

}
