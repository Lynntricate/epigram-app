package com.lynn.epigramapp.service;

import com.lynn.epigramapp.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    @Value("${JWT_SECRET}")
    private String secret;

    /**
     * Generate a token for a given user with the provided secret, which should be defined externally
     * @param user User object for which a token is generated
     * @return String token for the user
     */
    public String generateToken(User user) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60*60*1000))
                .signWith(key)
                .compact();
    }
}