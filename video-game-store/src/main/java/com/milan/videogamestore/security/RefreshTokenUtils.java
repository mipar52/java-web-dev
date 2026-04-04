package com.milan.videogamestore.security;

import com.milan.videogamestore.model.auth.RefreshToken;
import com.milan.videogamestore.model.users.AppUser;
import com.milan.videogamestore.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
public class RefreshTokenUtils {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTtlSeconds;

    public RefreshTokenUtils(RefreshTokenRepository refreshTokenRepository, @Value("${jwt.refresh.ttlSeconds}") long refreshTtlSeconds) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTtlSeconds = refreshTtlSeconds;
    }

    public String issueRefreshToken(AppUser appUser) {
        String token = randomToken(48);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(appUser);
        refreshToken.setExpiresAt(Instant.now().plusSeconds(refreshTtlSeconds));
        refreshToken.setRevoked(false);
        System.out.println("Issued refresh token: " + refreshToken.getToken());
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    private static String randomToken(int bytes) {
        byte[] b = new byte[bytes];
        new SecureRandom().nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }
}
