package com.example.demo.dto.login;

public class LoginResponse {
    private String token;

    public LoginResponse(String token, String username) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
