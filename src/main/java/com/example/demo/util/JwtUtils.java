package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private String getEncodedSecret() {
        return Base64.getEncoder().encodeToString(jwtSecret.getBytes());
    }

    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getEncodedSecret())
                    .parseClaimsJws(token)
                    .getBody();
            
            // Получаем ID из claims, а не из subject
            return claims.get("userId", Long.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
} 