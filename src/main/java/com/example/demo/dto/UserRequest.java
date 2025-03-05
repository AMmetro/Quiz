package com.example.demo.dto;

import com.example.demo.entity.UserEntity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRequest {
    
    @NotBlank(message = "user name can not be empty")
    @Size(min = 3, max = 50, message = "Length of name must be from 3 to 50 symbol")
    private String username;

    @NotNull(message = "Age can not be empty")
    private Long age;

    @NotBlank(message = "Password can not be empty")
    @Size(min = 6, message = "password must be more then 6 number")
    private String password;

    // Геттеры и сеттеры
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserEntity toEntity() {
        UserEntity entity = new UserEntity();
        entity.setUsername(this.username);
        entity.setPassword(this.password);
        entity.setAge(this.age);
        return entity;
    }
}
