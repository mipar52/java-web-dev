package com.milan.videogamestore.controller;

import com.milan.videogamestore.repository.GameCoverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MediaController {
    private final GameCoverRepository gameCoverRepository;

    @GetMapping("/media/db-covers/{gameId}")
    public ResponseEntity<byte[]> cover(@PathVariable Long gameId) {
        var cover = gameCoverRepository.findById(gameId).orElse(null);
        if (cover == null) return ResponseEntity.notFound().build();

        MediaType mt;
        try {
            mt = MediaType.parseMediaType(cover.getContentType());
        } catch (Exception ex) {
            mt = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mt)
                .body(cover.getData());
    }
}
