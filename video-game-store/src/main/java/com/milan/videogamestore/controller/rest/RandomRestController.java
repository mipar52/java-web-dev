package com.milan.videogamestore.controller.rest;

import com.milan.videogamestore.config.security.JwtService;
import com.milan.videogamestore.model.auth.LoginRequest;
import com.milan.videogamestore.model.auth.TokenResponse;
import com.milan.videogamestore.model.game.Game;
import com.milan.videogamestore.model.users.AppUser;
import com.milan.videogamestore.repository.AppUserRepository;
import com.milan.videogamestore.repository.GameRepository;
import com.milan.videogamestore.repository.LogEntryRepository;
import com.milan.videogamestore.security.RefreshTokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RandomRestController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenUtils refreshTokenUtils;
    private final JwtService jwtService;
    private final GameRepository gameRepository;
    private final LogEntryRepository logEntryRepository;


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> apiLogin(@RequestBody LoginRequest loginRequest) {
        AppUser appUser = appUserRepository.findByUsername(loginRequest.getUsername()).orElseThrow();
        if (!passwordEncoder.matches(loginRequest.getPassword(), appUser.passwordHash)) {
            throw new RuntimeException("Invalid credentials!");
        }

        String accessToken = jwtService.generateAccessToken(appUser);
        String refreshToken = refreshTokenUtils.issueRefreshToken(appUser);
        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    @GetMapping("/games")
    ResponseEntity<List<Game>> apiGameList() {
        return ResponseEntity.ok(gameRepository.findAll());
    }

    @GetMapping(value = "/admin/logs.csv", produces = "text/csv")
    public ResponseEntity<byte[]> exportCsv() {
        var rows = logEntryRepository.findAll();

        StringBuilder sb = new StringBuilder();
        sb.append("id,createdAt,username,ipAddress,success,reason,userAgent\n");

        for (var r : rows) {
            sb.append(csv(r.getId())).append(",")
                    .append(csv(r.getLoggedInAt())).append(",")
                    .append(csv(r.getUsername())).append(",")
                    .append(csv(r.getIpAddress())).append(",")
                    .append(csv(r.isSuccess())).append(",")
                    .append(csv(r.getFailureReason())).append(",")
                    .append(csv(r.getUserAgent()))
                    .append("\n");
        }

        byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"auth-logs.csv\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(data);
    }

    private String csv(Object v) {
        if (v == null) return "\"\"";
        String s = String.valueOf(v).replace("\"", "\"\"");
        return "\"" + s + "\"";
    }
}
