package com.example.demo.dto;

import com.example.demo.entity.UserEntity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "user email can not be empty")
    @Size(min = 3, max = 50, message = "Length of email must be from 3 to 50 symbol")
    private String email;

//    @NotNull(message = "Age can not be empty")
    private Long age;

    @NotNull(message = "String can not be empty")
    private String login;

    @NotBlank(message = "Password can not be empty")
    @Size(min = 6, message = "password must be more then 6 number")
    private String password;



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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserEntity toEntity() {
        UserEntity entity = new UserEntity();
        entity.setPassword(this.password);
        entity.setAge(this.age);
        return entity;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
