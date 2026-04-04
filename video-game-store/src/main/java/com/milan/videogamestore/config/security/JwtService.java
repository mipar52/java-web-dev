package com.milan.videogamestore.config.security;

import com.milan.videogamestore.model.users.AppUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey secretKey;
    private final Long secondsValid;

    public JwtService(@Value("${jwt.secret.key}") String secretKey, @Value("${jwt.seconds.valid}") Long secondsValid) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.secondsValid = secondsValid;
    }

    public String generateAccessToken(AppUser appUser) {
        Instant now = Instant.now();
        Instant expiryTime = now.plusSeconds(secondsValid);
        System.out.println("JWT username=" + appUser.getUsername() + " role=" + appUser.getRole());

        return Jwts.builder()
                .subject(appUser.username)
                .claim("role", appUser.getRole().getName())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiryTime))
                .signWith(secretKey)
                .compact();
    }

    public String validateAndGetSubject(String jwt) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }

    public String validateAndGetRole(String jwt) {
        Object roleObject = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .get("role");

        return roleObject != null ? roleObject.toString() : null;
    }
}
