package com.example.demo.util.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

@Service
public class JwtTokenService {

    private static final Logger logger = Logger.getLogger(JwtTokenService.class.getName());

    @Value("${jwt.secret}")
    private String secret;

    private String getEncodedSecret() {
        return Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String generateToken(String login, String userId, Long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        logger.info("Generating token for user: " + login + ", userId: " + userId);
        return createToken(claims, login, expiration);
    }

    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                    .signWith(SignatureAlgorithm.HS512, getEncodedSecret())
                    .compact();
        } catch (Exception e) {
            logger.severe("Error creating token: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

//    public Boolean validateToken(String token, String username) {
//        final String tokenUsername = extractUsername(token);
//        return (tokenUsername.equals(username) && !isTokenExpired(token));
//    }

    public String extractUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String userId = claims.get("userId", String.class);
            logger.info("Extracted userId from token: " + userId);
            return userId;
        } catch (Exception e) {
            logger.severe("Error extracting userId from token: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getEncodedSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.severe("Error parsing token: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
} 