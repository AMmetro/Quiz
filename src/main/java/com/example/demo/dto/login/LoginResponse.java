package com.example.demo.dto.login;

import java.util.HashMap;
import java.util.Map;

public class LoginResponse {

    private String accessToken;
    private String refreshToken;

    public LoginResponse(String accesToken, String refreshToken) {
        this.accessToken = accesToken;
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Map<String, String> getAccessToken() {
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("accessToken", accessToken);
        return responseMap;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
